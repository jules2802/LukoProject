window.onload=function (){
    var infoLatLon = document.getElementById("info");
    infoLatLon.onsubmit=function () {
        return checkIfFormIsCompleteCorrectly();

    };
};

function checkIfFormIsCompleteCorrectly(){

    var long=document.getElementById("longitude").value;
    var lat= document.getElementById("latitude").value;

    if(long.includes(",") || lat.includes(",")){
        window.alert("Vous devez mettre un point dans les coordonnées");
        return false;}
    else{

        if (long>-5.1530 & long < 8.2243){
            if(lat<51.089654 & lat > 42.33187){
                return true;
            }else{
                window.alert("Ce n'est pas une latitude de France Metropoolitaine");
                return false;
            }
        }else{
            window.alert("Ce n'est pas une longitute de France Metropolitaine");
            return false;
        }
    }
    }


