# IDEA 运行 sh 文件使用指南：
# 1. 首先安装 Git，记住 bash.exe 路径，例如（C:\Program Files\Git\bin\bash.exe）
# 2. 在 setting 中找到 terminal 设置，配置 Shell path，路径指定以上 bash.exe ↑
# 3. 点击 sh 文件右键，Run~

# 周几（0 代表周末）
date="$(date +%w)"
# 日期计算
#today="$(date +%Y-%m-%d --date="-1 day")"
onwReduce=$[$date-1]

# 如果是周末，就统计上一整周的数据
if (( $onwReduce==0 )); then
    onwReduce="7"
    echo $onwReduce
fi

twoReduce=$[-7-$onwReduce]
echo $twoReduce

# 计算出两次的具体时间，直接在 unionHql 中无法计算
oneReduceDate="$(date +%Y-%m-%d --date="-$onwReduce day")"
echo $oneReduceDate
twoReduceDate="$(date +%Y-%m-%d --date="$twoReduce day")"
echo $twoReduceDate

unionHql="select count(a.hotelId) from ods_htl_htlvendorpricemdb.google_hotelinfo a inner join (select hotelId from ods_htl_htlvendorpricemdb.google_hotelinfo where d = '${zdt.addDay(-7).format("yyyy-MM-dd")}' and datachange_lasttime > '$twoReduceDate' and datachange_lasttime < '${zdt.addDay(-7).format("yyyy-MM-dd")}') b on a.hotelId = b.hotelId where a.d = '${zdt.addDay(0).format("yyyy-MM-dd")}' and datachange_lasttime > '$oneReduceDate' and datachange_lasttime < '${zdt.addDay(0).format("yyyy-MM-dd")}'";
echo $unionHql

# 单个字段查询 Hql
singleTableHql="";
# 监控字段的数组
monitorFieldsArr=(address longitude latitude)

function monitorFieldHqlFunc() {
  singleTableHql="select count(a.$1) from ods_htl_htlvendorpricemdb.google_hotelinfo a inner join (select hotelId,$1 from ods_htl_htlvendorpricemdb.google_hotelinfo where d='${zdt.addDay(-7).format("yyyy-MM-dd")}' and datachange_lasttime > '$twoReduceDate' and datachange_lasttime < '${zdt.addDay(-7).format("yyyy-MM-dd")}') b on a.hotelId = b.hotelId and a.$1 = b.$1 where a.d = '${zdt.addDay(0).format("yyyy-MM-dd")}' and datachange_lasttime > '$oneReduceDate' and datachange_lasttime < '${zdt.addDay(0).format("yyyy-MM-dd")}'";
}

# HQL 语句拼接
for ((i=0; i<${#monitorFieldsArr[@]}; i++))
do
    monitorFieldHqlFunc "${monitorFieldsArr[i]}"
    unionHql="${unionHql} union all ${singleTableHql}"
done
echo ---------"执行的 hql："${unionHql}---------------

# 执行 HQL，并且获取每个字段的值
fieldTotalCount=`hive -e "set hive.cli.print.header=false;${unionHql}"`

# 将数字转化成字符串
fieldTotalCountStr="$fieldTotalCount"
# 将数字转换成数组
fieldTotalArr=($fieldTotalCountStr)
echo ----------"数组值："${fieldTotalArr[*]} "数组"${#fieldTotalArr[@]}---------------------

# hotelId 总量
hotelIdCount=""
# 计算每个字段的环比并赋值
for ((i=0; i<${#fieldTotalArr[@]}; i++))
do
    if [ $i -eq 0 ];
    then
      hotelIdCount=${fieldTotalArr[i]}
    else
      # 字段环比值
      fieldMom=$(awk 'BEGIN{printf "%.2f",'$[$hotelIdCount-${fieldTotalArr[i]}]'*100/'$hotelIdCount'}')%
      echo -------------"字段环比值："${fieldMom}-----------------------
      # HQL 执行
      hive -v -e "insert into table htlcrawleroctopus_db.crawler_data_change_statistic_report partition(d='${zdt.addDay(0).format("yyyy-MM-dd")}')
      values ('google_hotelinfo','${monitorFieldsArr[$[$i-1]]}','$fieldMom','${zdt.addDay(-1).format("yyyy-MM-dd")}')"
    fi
done
