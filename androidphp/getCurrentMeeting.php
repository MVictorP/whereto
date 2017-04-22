<?php
if ($_SERVER["REQUEST_METHOD"] == "POST") {
    require 'conn.php';
    getCurrentMeeting();
}

function getCurrentMeeting() {
    global $connect;
    
    $query = "SELECT Meeting_ID FROM meetings ORDER BY Meeting_ID DESC LIMIT 0,1";
    $result = mysqli_query($connect, $query);
   

    $temp_array = array();
    
    $row = mysqli_fetch_assoc($result);
    if(!$row){
        $temp_array[] = array("Meeting_ID" => "0");
    }else{
       $temp_array[] = $row; 
    }


    header('Content-Type: application/json');
    echo json_encode(array("mtngid" => $temp_array));
    mysqli_close($connect);
}

