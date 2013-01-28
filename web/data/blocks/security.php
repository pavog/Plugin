<?php

@include ('../page/blocks/token_assert.php');

if(isset($_COOKIE['StatsLogin'])) {
	
	$login = $_COOKIE['StatsLogin']; 
	$pass = $_COOKIE['StatsPass'];
	
} else {
	$login = "Guest";
	$pass = "";
}

if($curPage == "settings") {
	if(!isset($_COOKIE['StatsLogin']) ||
	($login == "Guest" && $pass == "") ||
	($login != CONFIG_USER || $pass != CONFIG_PASS)) {
		header("Location: ./?view=login&origin=settings");
	}
} else if ($curPage == "login") {
	if (isset($_POST['signin'])) {
		if ($_POST['cuser'] != CONFIG_USER || $_POST['cpass'] != CONFIG_PASS) {
			echo "<div class='alert alert-error'>";
			echo "<button type='button' class='close' data-dismiss='alert'>&times;</button>";
			echo "<strong>Error!</strong> Invalid username-password pair";
			echo "</div>";
		} else {
		 	$hour = time() + 3600; 
			setcookie(StatsLogin, $_POST['cuser'], $hour);
			setcookie(StatsPass, $_POST['cpass'], $hour);
			
			$origin = $_POST['origin'];
			header("Location: ./?view=".$origin); 
	 	} 
	}
} else if ($curPage == "logout") {
	$past = time() - 100; 
	setcookie(StatsLogin, gone, $past); 
	setcookie(StatsPass, gone, $past); 
	header("Location: ./?view=main"); 
}
?>