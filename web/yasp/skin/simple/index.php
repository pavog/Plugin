<?php
$username = $_GET['username'];
$redirectString = 'Location: ../index.php?a=-25&w=35&wt=-45&ratio=20&format=png&displayHairs=true&headOnly=false&login='.$username;

header( $redirectString ) ;

?>