<?php

//session_start();
if ($_SERVER["REQUEST_METHOD"] == "POST") {
    require 'conn.php';
    updateAttendance();
}

function updateAttendance() {
    global $connect;

    $employee_id = $_POST["Employee_ID"];
    $meeting_id = $_POST["Meeting_ID"];
    $response_datetime = $_POST["Response_DateTime"];
    $response_type_id = $_POST["Response_Type_ID"];
    
    $query = "UPDATE attendance SET Response_DateTime='$response_datetime', "
            . "Response_Type_ID='$response_type_id' WHERE Employee_ID='$employee_id' AND Meeting_ID='$meeting_id';";
    
    mysqli_query($connect, $query);
    
    if(mysqli_affected_rows($connect) > 0){
        $json['success'] = 'Reply to invitation was successful';
        echo json_encode($json);
    } else{
        $json['error'] = 'There was an error';
        echo json_encode($json);
    }

    //$_SESSION['meetingID'] = mysqli_insert_id($connect);
    //$mtngid[] = array("meetid" => (string)$_SESSION['meetingID']);
    //echo json_encode(array("mtngid" => $mtngid));
    mysqli_close($connect);
}

