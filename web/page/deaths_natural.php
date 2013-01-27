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
    		<th>Victim</th>
    		<th>Time</th>
	</tr>
	</thead>
	<tbody>
	
	<?php
	
               $pag->items_total = $serverObj->getTotalOtherKills();  
                $pag->paginate();
                $pagination = $pag->display_pages();
                
                $i = $pag->low;
                
                $query = $serverObj->getOtherKills($pag->limit);
                
                while($row = mysql_fetch_assoc($query)) {
                    $i++;
                    
                    echo '<tr>';
                    echo '<td>';
                    echo $i;
                    echo '</td>';                   
                    echo '<td>';
                    echo $row['killed'];
                    echo '</td>';                  
                    echo '<td>';
                    echo $row['type'];
                    echo '</td>';
                    echo '<td>';
                    echo QueryUtils::formatDate($row['time']);
                    echo '</td>';
                    echo '</tr>';
                }
	
	?>
	
	</tbody>
</table>