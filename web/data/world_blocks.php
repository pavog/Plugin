<?php @include ('../data/blocks/token_assert.php'); ?>

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
		$blockName = str_replace(" ", "%20", $row['name']);
		echo "<tr>";
		echo "<td style='text-align: center;'><img src='../data/util/block/?name=".$blockName."' alt='".$row['name']."' /></td>";
		echo "<td style='text-align: center;'>".$row['destroyed']."</td>";
		echo "<td style='text-align: center;'>".$row['placed']."</td>";
		echo "</tr>";
	}
	?>
	
	</tbody>
</table>