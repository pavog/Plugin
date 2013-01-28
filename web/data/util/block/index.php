<?php
$block = $_GET['name'];
$block = strtolower($block);
$block = str_replace(" ", "", $block);
$block = str_replace("-", "", $block);
$block = str_replace("'", "", $block);
$block = str_replace("\"", "", $block);
$block = str_replace("%20", "", $block);

$redirectString = 'Location: ./img/'.$block.".png";

header( $redirectString ) ;

?>