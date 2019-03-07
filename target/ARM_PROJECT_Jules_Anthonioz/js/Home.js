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

        if (long>-5.15 & long < 8.22){
            if(lat<51.08 & lat > 42.33){
                return true;
            }else{
                window.alert("Ce n'est pas une latitude de France Metropoolitaine, elle doit être comprise entre 42.33 et 51.08");
                return false;
            }
        }else{
            window.alert("Ce n'est pas une longitute de France Metropolitaine, elle doit être comprise entre -5.15 et 8.22");
            return false;
        }
    }
    }


