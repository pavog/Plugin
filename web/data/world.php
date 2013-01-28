<?php @include ('../data/blocks/token_assert.php'); ?>

<?php

if(!isset($_GET['details'])) {

?>

<ul class="breadcrumb">
	<li><a href="../">Dashboard</a> <span class="divider">/</span></li>
	<li class="active">World</li>
</ul>

<?php include ('./data/blocks/notifications.php'); ?>

<div class="row-fluid">

	<div class="span6">
	<h2>Blocks</h2>
	
		<?php include ('./data/world_blocks.php'); ?>

	</div>
	
	<div class="span6">
	<h2>Items</h2>
	
		<?php include ('./data/world_items.php'); ?>
		
	</div>
	
</div>

<?php

} else {
	$details = $_GET['details'];
	
?>

<ul class="breadcrumb">
	<li><a href="../">Dashboard</a> <span class="divider">/</span></li>
	<li><a href="../?view=world">World</a> <span class="divider">/</span></li>
	<li class="active"><?php echo $details; ?></li>
</ul>

<?php
	if($details == "Blocks") {
		echo "<div class='row-fluid'>";
		echo "<div class='span12'>";
		echo "<h2>Global Block Statistics</h2>";
		include ('./data/world_blocks.php');
		echo "</div>";
		echo "</div>";
	} else if($details == "Items") {
		echo "<div class='row-fluid'>";
		echo "<div class='span12'>";
		echo "<h2>Global Item Statistics</h2>";
		include ('./data/world_items.php');
		echo "</div>";
		echo "</div>";
	} else {
		echo "<div class='alert alert-error'>";
		echo "<button type='button' class='close' data-dismiss='alert'>&times;</button>";
		echo "<strong>404</strong> The requested page does not exist";
		echo "</div>";
	}
?>

<?php 

}

?>