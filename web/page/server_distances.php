<p><strong>Total Travel Distance:</strong> <?php echo(QueryUtils::formatDistance($serverObj->getDistanceTraveledTotal())); ?></p>
<p><strong>Total Foot Travel Distance:</strong> <?php echo(QueryUtils::formatDistance($serverObj->getDistanceTraveledByFootTotal())); ?></p>
<p><strong>Total Minecart Travel Distance:</strong> <?php echo(QueryUtils::formatDistance($serverObj->getDistanceTraveledByMinecartTotal())); ?></p>
<p><strong>Total Boat Travel Distance:</strong> <?php echo(QueryUtils::formatDistance($serverObj->getDistanceTraveledByBoatTotal())); ?></p>
<p><strong>Total Pig Travel Distance:</strong> <?php echo(QueryUtils::formatDistance($serverObj->getDistanceTraveledByPigTotal())); ?></p>