# IDEA 运行 sh 文件使用指南：
# 1. 首先安装 Git，记住 bash.exe 路径，例如（C:\Program Files\Git\bin\bash.exe）
# 2. 在 setting 中找到 terminal 设置，配置 Shell path，路径指定以上 bash.exe ↑
# 3. 点击 sh 文件右键，Run~

today="$(date +%Y-%m-%d --date="0 day")"
yesterday="$(date +%Y-%m-%d --date="-1 day")"
echo "==============$yesterday========="

singleTableHql="";
unionHql="";

function sigleTableHqlFunc() {
    singleTableHql="select '$2' as table_name, '$4' as field_name, if(nvl(cast((t1.total-t2.change)/t1.total*100 as decimal(10,2)) + 0,null) is null, 0 , cast((t1.total-t2.change)/t1.total*100 as decimal(10,2))) as mom, '$yesterday' as data_date, t1.total as total_count, t2.change as change_count
    from (select count(a.$3) as total from $1.$2 a inner join (select hotelId from $1.$2 where d = '$yesterday') b on a.$3 = b.$3 where a.d = '$today' and datachange_lasttime > '$yesterday' and datachange_lasttime < '$today') t1,
    (select count(a.$4) as change from $1.$2 a inner join (select $3,$4 from $1.$2 where d = '$yesterday') b on a.$3 = b.$3 and a.$4 = b.$4 where a.d = '$today' and datachange_lasttime > '$yesterday' and datachange_lasttime < '$today') t2"
}

# 第一组
databaseName="ods_htl_htlvendorpricemdb"
tableNameArr=(google_hotelinfo)
uniqueField="hotelId"
fieldArr=(address longitude latitude)
# 拼接hql
for ((i=0; i<${#tableNameArr[@]}; i++))
do
    for ((j=0; j<${#fieldArr[@]}; j++))
    do
        sigleTableHqlFunc "${databaseName}" "${tableNameArr[i]}" "${uniqueField}" "${fieldArr[j]}"
        if [[ $i -eq $((${#tableNameArr[@]}-1)) ]] && [[ $j -eq $((${#fieldArr[@]}-1)) ]] ; then
            unionHql=${unionHql}${singleTableHql}
        else
            unionHql="${unionHql}${singleTableHql} union all "
        fi
    done
done

echo "---------执行的 unionHql ：${unionHql}"

hive -v -e "
insert overwrite table htlcrawleroctopus_db.crawler_data_change_statistic_report partition(d='${today}')
SELECT table_name, field_name, mom, data_date, total_count, change_count FROM (${unionHql}) as t
"
