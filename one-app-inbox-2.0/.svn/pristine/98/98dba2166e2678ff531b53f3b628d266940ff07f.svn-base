function setRadioButtonValue(lable) {

    var id = lable.split("@")[0]
    var name = lable.split("@")[1]
    $("input[name=" + name + "]").each(function(idx) {
         $(this).val(" ")

    });
    $('#'+id).attr("value","radio@"+lable.split("@")[0])
}

function setRadioButtonValue1(lable) {

    var id = lable.split("@")[1]
    var name = lable.split("@")[0]
    var divID =name+id;

    $("input[name=" + name + "]").each(function(idx) {
         $(this).val(" ")

    });

    $('#'+divID).attr("value",""+id)
}

function setCheckButtonValue(lable) {
    $('#'+lable).attr("value","check@"+lable);
  if($('#'+lable).is(":checked")){
     $('#'+lable).attr("value","check@"+lable)
  }
}

