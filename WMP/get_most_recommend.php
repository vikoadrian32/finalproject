<?php
header('Content-Type: application/json');
include("dbconnect.php"); 

$query = "
WITH sumPoint AS (
    SELECT product_id, SUM(point) AS p
    FROM recommend
    GROUP BY product_id
), 
getProductID AS (
    SELECT product_id, p
    FROM sumPoint
    WHERE p > 1
),
getMost AS (
SELECT DISTINCT p,
DENSE_RANK() OVER (ORDER BY p DESC) AS rank
FROM getProductID
ORDER BY p DESC
LIMIT 3)
   SELECT gp.product_id, gm.rank
   FROM getProductID AS gp JOIN getMost AS gm ON gp.p = gm.p 
";


$result = mysqli_query($conn, $query);

if ($result) {
    $products = [];
    
    while ($row = mysqli_fetch_assoc($result)) {
        $products[] = $row;
    }
    
    echo json_encode([
        'status' => 'success',
        'data' => $products
    ]);
} else {
    
    echo json_encode([
        'status' => 'error',
        'message' => mysqli_error($conn)
    ]);
}


mysqli_close($conn);
?>

