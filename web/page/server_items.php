<?php

$mostPickedUp = $serverObj->getMostPickedUp();
$mostPickedUpName = QueryUtils::getResourceNameById($mostPickedUp);  
$mostPickedUpName = strtolower($mostPickedUpName);
$mostPickedUpName = str_replace(" ", "", $mostPickedUpName);
$mostPickedUpNum = $serverObj->getPickedUpOfTypeTotal($mostPickedUp);

$mostDropped = $serverObj->getMostDropped();
$mostDroppedName = QueryUtils::getResourceNameById($mostDropped); 
$mostDroppedName = strtolower($mostDroppedName);
$mostDroppedName = str_replace(" ", "", $mostDroppedName);
$mostDroppedNum = $serverObj->getDroppedOfTypeTotal($mostDropped);

?>

	<dl class="dl-horizontal">
		<dt style="width: 110px !important;"><span class="badge badge-success"><i class="icon-plus"></i> <?php echo($serverObj->getPickedUpTotal()); ?></span></dt>
		<dd style="margin-left: 130px !important;">Total Picked up</dd>
	</dl>
	<dl class="dl-horizontal">
		<dt style="width: 110px !important;"><span class="badge badge-success"><?php echo "<img src='../src/img/block/".$mostPickedUpName.".png' width='15px' height='15px' alt='".$mostPickedUp."' title='".$mostPickedUpNum." times' /> ".$mostPickedUpNum; ?></span></dt>
		<dd style="margin-left: 130px !important;">Most Picked up</dd>
	</dl>
	<dl class="dl-horizontal">
		<dt style="width: 110px !important;"><span class="badge badge-important"><i class="icon-minus"></i> <?php echo($serverObj->getDroppedTotal()); ?></span></dt>
		<dd style="margin-left: 130px !important;">Total Dropped</dd>
	</dl>
	<dl class="dl-horizontal">
		<dt style="width: 110px !important;"><span class="badge badge-important"><?php echo "<img src='../src/img/block/".$mostDroppedName.".png' width='15px' height='15px' alt='".$mostDropped."' title='".$mostDroppedNum." times' /> ".$mostDroppedNum; ?></span></dt>
		<dd style="margin-left: 130px !important;">Most Dropped</dd>
	</dl>