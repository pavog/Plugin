<?php
$mob = $_GET['name'];
$mob = strtolower($mob);
$mob = str_replace(" ", "", $mob);
$mob = str_replace("-", "", $mob);
$mob = str_replace("'", "", $mob);
$mob = str_replace("\"", "", $mob);
$mob = str_replace("%20", "", $mob);

$redirectString = 'Location: ./img/'.$mob.".png";

header( $redirectString ) ;

?>