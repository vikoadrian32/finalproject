<?php
header('Content-Type: application/json');
include('dbconnect.php');

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    $user_id = $_POST['user_id']; 
    $sql = "DELETE FROM cart WHERE user_id = ?";
    
    if ($stmt = $conn->prepare($sql)) {
        $stmt->bind_param("i", $user_id);
        if ($stmt->execute()) {
            echo json_encode(["success" => true, "message" => "Cart cleared successfully"]);
        } else {
            echo json_encode(["success" => false, "message" => "Failed to clear cart"]);
        }
        $stmt->close();
    } else {
        echo json_encode(["success" => false, "message" => "Error in query"]);
    }
    $conn->close();
}
?>
