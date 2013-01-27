<?php

$mostPlaced = $serverObj->getBlocksMostPlaced();
$mostPlacedName = QueryUtils::getResourceNameById($mostPlaced);  
$mostPlacedName = strtolower($mostPlacedName);
$mostPlacedName = str_replace(" ", "", $mostPlacedName);
$mostPlacedNum = $serverObj->getBlocksPlacedOfTypeTotal($mostPlaced);

$mostDestroyed = $serverObj->getBlocksMostDestroyed();
$mostDestroyedName = QueryUtils::getResourceNameById($mostDestroyed); 
$mostDestroyedName = strtolower($mostDestroyedName);
$mostDestroyedName = str_replace(" ", "", $mostDestroyedName);
$mostDestroyedNum = $serverObj->getBlocksPlacedOfTypeTotal($mostDestroyed);

?>

	<dl class="dl-horizontal">
		<dt style="width: 110px !important;"><span class="badge badge-success"><i class="icon-plus"></i> <?php echo($serverObj->getBlocksPlacedTotal()); ?></span></dt>
		<dd style="margin-left: 130px !important;">Total Placed</dd>
	</dl>
	<dl class="dl-horizontal">
		<dt style="width: 110px !important;"><span class="badge badge-success"><?php echo "<img src='../src/img/block/".$mostPlacedName.".png' width='15px' height='15px' alt='".$mostPlaced."' title='".$mostPlacedNum." times' /> ".$mostPlacedNum; ?></span></dt>
		<dd style="margin-left: 130px !important;">Most Placed</dd>
	</dl>
	<dl class="dl-horizontal">
		<dt style="width: 110px !important;"><span class="badge badge-important"><i class="icon-minus"></i> <?php echo($serverObj->getBlocksDestroyedTotal()); ?></span></dt>
		<dd style="margin-left: 130px !important;">Total Destroyed</dd>
	</dl>
	<dl class="dl-horizontal">
		<dt style="width: 110px !important;"><span class="badge badge-important"><?php echo "<img src='../src/img/block/".$mostDestroyedName.".png' width='15px' height='15px' alt='".$mostDestroyed."' title='".$mostDestroyedNum." times' /> ".$mostDestroyedNum; ?></span></dt>
		<dd style="margin-left: 130px !important;">Most Destroyed</dd>
	</dl>