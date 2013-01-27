<table class="table table-bordered table-hover tablesorter">
	<thead>
	<tr>
		<th style="text-align: center;">Block Type</th>
		<th style="text-align: center;">Destroyed</th>
		<th style="text-align: center;">Placed</th>
	</tr>
	</thead>
	<tbody>
	
	<?php
	$query = QueryUtils::getBlockList();
	
	while($row = mysql_fetch_assoc($query)) {
		if($row['name'] == "") continue;
		$blockName = strtolower($row['name']);
		$blockName = str_replace(" ", "", $blockName);
		$blockName = str_replace("'", "f", $blockName);
		echo "<tr>";
		echo "<td style='text-align: center;'><img src='../src/img/block/".strtolower($blockName).".png' alt='".$blockName."' /></td>";
		echo "<td style='text-align: center;'>".$row['destroyed']."</td>";
		echo "<td style='text-align: center;'>".$row['placed']."</td>";
		echo "</tr>";
	}
	?>
	
	</tbody>
</table>