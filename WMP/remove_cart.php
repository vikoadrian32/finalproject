<?php
header('Content-Type: application/json');
include('dbconnect.php');

$product_id = $_POST['product_id'];

$query = "DELETE FROM cart WHERE product_id = '$product_id' LIMIT 1";
if (mysqli_query($conn, $query)) {
    echo json_encode(['success' => true]);
} else {
    echo json_encode(['success' => false, 'message' => 'Failed to remove product']);
}
?>
