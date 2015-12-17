<?php

// array for JSON response
$response = array();

require_once __DIR__ . '/db_connect.php';

$db = new DB_CONNECT();

if (isset($_GET["num"])) {
    $pid = $_GET['num'];

    // get a product from keys table
    $result = mysql_query("SELECT *FROM keys WHERE num = $num");

    if (!empty($result)) {
        // check for empty result
        if (mysql_num_rows($result) > 0) {

            $result = mysql_fetch_array($result);

            $product = array();
            $product["num"] = $result["num"];
            $product["encoded"] = $result["encoded"];
            $product["exponend"] = $result["exponend"];
            $product["modulus"] = $result["modulus"];
            
            $response["success"] = 1;

            $response["product"] = array();

            array_push($response["product"], $product);

            echo json_encode($response);
        } else {
            // no key found
            $response["success"] = 0;
            $response["message"] = "No key found";

            echo json_encode($response);
        }
    } else {
        $response["success"] = 0;
        $response["message"] = "No key found";

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