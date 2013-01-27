<?php 
$pag = new Paginator();
$pag->mid_range = 5;
$pag->items_per_page = 30;
?>

<table class="table table-bordered table-hover tablesorter">
	<thead>
	<tr>
    		<th></th>
    		<th>Killer</th>
    		<th>Weapon</th>
    		<th>Victim</th>
    		<th>Time</th>
	</tr>
	</thead>
	<tbody>
	
	<?php
	
	$pag->items_total=$serverObj->getTotalPVEKills();
	$pag->paginate();
	$pagination=$pag->display_pages();
	
	$i=$pag->low;
	
	$query=$serverObj->getPVEKills($pag->limit);
	
	while($row=mysql_fetch_assoc($query)){
	
		$i++;
		
		echo'<tr>';
		echo'<td>';
		echo$i;
		echo'</td>';
		if($row['killer']!='Player'){
			echo '<td style="text-align:center">';
			echo '<img src="../src/img/mob/'.strtolower($row['killer']).'.png" style="width:20px;height:20px;" title="'.$row['killer'].'" />';
			echo '</td>';
		}
		else{
			echo '<td>';
			echo '<a href="?view=player&username='.$row['killer_player'].'">';
			echo $row['killer_player'];
			echo '</a>';
			echo '</td>';
		}
		echo'<td>';
		if($row['weapon'] == "None") { echo $row['weapon']; }
		else {
			$weaponName = strtolower($row['weapon']);
			$weaponName = str_replace(" ", "", $weaponName);
			$weaponName = str_replace("'", "f", $weaponName);
			$weaponName = strtolower($weaponName);
			echo '<img src="../src/img/block/'.$weaponName.'.png" style="width:20px;height:20px;" title="'.$weaponName.'" />';
		}
		echo'</td>';
		if($row['killed']!='Player'){
			echo' <td>';
			echo $row['killed'];
			echo '</td>';
		}
		else {
			echo '<td>';
			echo '<a href="?view=player&username='.$row['killed_player'].'">';
			echo $row['killed_player'];
			echo '</a>';
			echo '</td>';
		}
		echo'<td>';
		echo QueryUtils::formatDate($row['time']);
		echo'</td>';
		echo'</tr>';
		
	}
	
	?>
	
	</tbody>
</table>