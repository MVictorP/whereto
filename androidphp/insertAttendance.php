<?php

if ($_SERVER["REQUEST_METHOD"] == "POST") {
    require 'conn.php';
    insertAttendance();
}

function insertAttendance() {
    
    global $connect;
  
    $employee_id = $_POST["Employee_ID"];
    $meet_id = $_POST["Meeting_ID"];
      
    $query = " INSERT into attendance(Employee_ID, Meeting_ID) "
            . "values ('$employee_id', '$meet_id');";
    mysqli_query($connect, $query);
    
    if(mysqli_affected_rows($connect) > 0){
        $json['success'] = 'Employee(s) successfully invited';
        echo json_encode($json);
    } else{
        $json['error'] = 'There was an error';
        echo json_encode($json);
    }
    mysqli_close($connect);
    
   
}
