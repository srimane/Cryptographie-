<?php

// array for JSON response
$response = array();

if (isset($_POST['encoded']) && isset($_POST['exponend']) && isset($_POST['modulus']) && isset($_POST['num'])) {
    
    $encoded = $_POST['encoded'];
    $exponend = $_POST['exponend'];
    $modulus = $_POST['modulus'];
	$num = $_POST['num'];

    require_once __DIR__ . '/db_connect.php';

    // connecting to db
    $db = new DB_CONNECT();

    // mysql inserting a new row
    $result = mysql_query("INSERT INTO keys(num, encoded, exponend, modulus) VALUES('$num', '$encoded', '$exponend', '$modulus')");

    if ($result) {
        $response["success"] = 1;
        $response["message"] = "key successfully created.";

        echo json_encode($response);
    } else {
        $response["success"] = 0;
        $response["message"] = "Oops! An error occurred.";
        
        echo json_encode($response);
    }
} else {
    // required field is missing
    $response["success"] = 0;
    $response["message"] = "Required field is missing";

    // echoing JSON response
    echo json_encode($response);
}
?>