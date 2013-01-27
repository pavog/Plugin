<ul class="breadcrumb">
	<li><a href="../">Dashboard</a> <span class="divider">/</span></li>
	<li class="active">Deaths</li>
</ul>

<?php include ('./page/blocks/notifications.php'); ?>

<div class="row-fluid">
	<div class="span6">
		<h2>Violent Deaths</h2>
		<?php include ('./page/deaths_violent.php'); ?>
	</div>
	
	<div class="span6">
		<h2>Natural Deaths</h2>
		<?php include ('./page/deaths_natural.php'); ?>
	</div>
</div>