// $(function () {
//     //初始化分页菜单按钮
//     $("#advantageMenu [name=pagingBox] [name=selectPagingBtn]").click(function () {
//         $("#advantageMenu").hide();
//         document.contentWindow.document.onclick = function (event) {
//             //获取xpath
//             var path = getXpath(event.target);
//             //清除点击事件
//             document.onclick = function () {
//             };
//             //回填xpath
//             $("#advantageMenu").show();
//             $("#advantageMenu [name=pagingBox] [name=xpath]").val(path);
//             // $("#pagingForm").data("bootstrapValidator").updateStatus('xpath', 'NOT_VALIDATED').validateField("xpath")
//         }
//     });
// });

//获取传入元素的xpath
function getXpath(e) {
    var eId = e.id;
    if (typeof eId === 'object') { //取到表单元素的情况下 例如 [@id='[object HTMLInputElement]']
        eId = e.attributes.id.value;
    }
    // if (eId && !containsNum(eId)) return `//*[@id='${eId}']`;
    if (e.tagName.toLowerCase() === 'body') return '//body';
    for (var t = 0,
             n = e.parentNode.childNodes,
             r = 0,
             i = n.length; r < i; r++) {
        var o = n[r];
        if (o == e) {
            var a = "";
            return a = `${arguments.callee(e.parentNode)}/${e.tagName.toLowerCase()}`,
                a = a + "[" + (t + 1) + "]"
        }
        1 == o.nodeType && o.tagName == e.tagName && t++
    }
}

// function containsNum(e){
//     return /\d/.test(e);
// }
