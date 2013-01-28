<?php @include ('../data/blocks/token_assert.php'); ?>

<ul class="breadcrumb">
	<li><a href="../">Dashboard</a> <span class="divider">/</span></li>
	<li class="active">Deaths</li>
</ul>

<?php include ('./data/blocks/notifications.php'); ?>

<?php 
	$pag = new Paginator();
	$pag->mid_range = 5;
	$pag->items_per_page = 30;
?>

<div class="row-fluid">
	<div class="span7">		
		<?php
			
		$pag->items_total=$serverObj->getTotalKills();
		$pag->paginate();
		$pagination=$pag->display_pages();
		
		$i=$pag->low;
		
		$query=$serverObj->getKills($pag->limit);
		
		while($row=mysql_fetch_assoc($query)){
			echo "<div class='well well-small'>";
			echo "<div class='row-fluid'>";
			echo "<div class='span5'>";
			
			$time = QueryUtils::formatDate($row['time']);
			$killerType = $row['killer'];
			$victimType = $row['killed'];
			$weapon = $row['weapon'];
			
			echo $time.": ";
			
			echo "</div>";
			echo "<div class='span3' style='text-align:right;'>";
			
			echo "<span class='label label-success'>";
			if($killerType == 'Player') {
				$killerName = $row['killer_player'];
				echo "<img src='../data/util/player/head/?username=".$killerName."' style='width:20px;height:20px;border:1px solid #000;' title='".$killerName."' alt='".$killerName."' /> ";
				echo "<a href='?view=player&amp;username=".$killerName."' style='color: white !important;'>".$killerName."</a>";
			} else {
				echo "<img src='../data/util/mob/?name=".str_replace(" ", "%20", $killerType)."' style='width:20px;height:20px;border:1px solid #000;' title='".$killerType."' alt='".$killerType."' /> ".$killerType;
			}
			echo "</span>";
			
			
			echo "</div>";
			echo "<div class='span1' style='text-align:center;'>";
			
			if($weapon == "None" || $weapon == "Hand") { 
				echo '<img src="../data/util/block/?name=none" style="width:20px;height:20px;" title="'.$weapon.'" alt="'.$weapon.'" />';
			}
			else {
				$weaponName = str_replace(" ", "%20", $weapon);
				echo '<img src="../data/util/block/?name='.$weaponName.'" style="width:20px;height:20px;" title="'.$weapon.'" alt="'.$weapon.'" />';
			}
			
			
			echo "</div>";
			echo "<div class='span3'>";
			
			echo "<span class='label label-important'>";
			if($victimType == 'Player') {
				$victimName = $row['killed_player'];
				echo "<img src='../data/util/player/head/?username=".$victimName."' style='width:20px;height:20px;border:1px solid #000;' title='".$victimName."' alt='".$victimName."' /> ";
				echo "<a href='?view=player&amp;username=".$victimName."' style='color: white !important;'>".$victimName."</a>";
			} else {
				echo "<img src='../data/util/mob/?name=".str_replace(" ", "%20", $victimType)."' style='width:20px;height:20px;border:1px solid #000;' title='".$victimType."' alt='".$victimType."' /> ".$victimType;
			}
			echo "</span>";
			
			
			echo "</div>";
			echo "</div>";
			echo "</div>";
			echo "\n";
		}
		
		?>
	</div>
	<div class="span5">
		<div class="well" style="height: 250px;">
			<?php include ('./data/deaths_stats.php'); ?>
		</div>
	</div>
</div>