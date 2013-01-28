<?php
$username = $_GET['username'];
$redirectString = 'Location: ../index.php?headOnly=true&displayHairs=false&ratio=4&login='.$username;

header( $redirectString ) ;

?>