<?php

//session_start();
if ($_SERVER["REQUEST_METHOD"] == "POST") {
    require 'conn.php';
    insertMeeting();
}

function insertMeeting() {
    global $connect;

    $meeting_name = $_POST["Meeting_Name"];
    $meeting_description = $_POST["Meeting_Description"];
    $meeting_datetime = $_POST["Meeting_DateTime"];
    $meeting_duration = $_POST["Meeting_Duration"];
    $meeting_dept_id = $_POST["Meeting_Dept_ID"];
    $meeting_room_id = $_POST["Meeting_Room_ID"];
    $organizer_id = $_POST["Organizer_ID"];

    $query = " INSERT into meetings(Meeting_Name, Meeting_Description, "
            . "Meeting_DateTime,Meeting_Duration, Meeting_Dept_ID, "
            . "Meeting_Room_ID,Organizer_ID) "
            . "values ('$meeting_name','$meeting_description',"
            . "'$meeting_datetime', '$meeting_duration', '$meeting_dept_id', "
            . "'$meeting_room_id', '$organizer_id');";

    mysqli_query($connect, $query);
    if(mysqli_affected_rows($connect) > 0){
        $json['success'] = 'Meeting successfully created';
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
