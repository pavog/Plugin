<div class="row-fluid">

<?php

if(!isset($_GET['username'])) {

echo "<ul class='breadcrumb'>";
echo "<li><a href='../'>Dashboard</a> <span class='divider'>/</span></li>";
echo "<li class='active'>Players</li>";
echo "</ul>";

include ('./page/blocks/notifications.php'); 
include ('./page/player_list.php');

} else {

echo "<ul class='breadcrumb'>";
echo "<li><a href='../'>Dashboard</a> <span class='divider'>/</span></li>";
echo "<li><a href='../?view=player'>Players</a> <span class='divider'>/</span></li>";
echo "<li class='active'>".$_GET['username']."</li>";
echo "</ul>";

include ('./page/blocks/notifications.php'); 
include ('./page/player_info.php');

}

?>