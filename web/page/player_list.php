<div class="span12">
<div class="search pull-right">
	<form action="./" method="get" class="form-search" style="padding-top: 15px;">
		<input type="hidden" name="view" value="player" />
		
    		<div class="input-append">
			<input type="text" name="username" placeholder="Search" class="span2" style="width: 150px;" />
			<input type="submit" value="Search" class="btn">
		</div>
	</form>
</div>

<h2>Player List</h2>

<?php
$pag = new Paginator();
$pag->mid_range = 5;
$pag->items_per_page = 30;
?>

<table class="table table-bordered table-hover tablesorter">
	<thead>
	<tr>
		<th>ID</th>
		<th>Name</th>
		<th>Last Seen</th>
		<th>Date Joined</th>
	</tr>
	</thead>
	<tbody>

	<?php
	$pag->items_total = $serverObj->getAllPlayers();
		$pag->paginate();
		$pagination = $pag->display_pages();
	
		$i = $pag->low;
	
		$query = $serverObj->getPlayers($pag->limit);
	
		while($row = mysql_fetch_assoc($query)) {
			$i++;
			
			$player = new PLAYER($row['uuid']);
			
			echo "<tr>";
			echo "<td>".$i."</td>";
			echo "<td><a href='?view=player&username=".$player->getName()."' >".$player->getName()."</a></td>";
			echo "<td>".QueryUtils::formatDate($player->getLastLogin())."</td>";
			echo "<td>".QueryUtils::formatDate($player->getFirstLogin())."</td>";
			echo "</tr>";
		}
	 ?>
	
	</tbody>
</table>
<?php
echo "<div class='pagination' id='pageSelector'><ul>";
echo $pagination;
echo "</ul></div>";
?>

</div>