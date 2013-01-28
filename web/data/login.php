<?php @include ('../data/blocks/token_assert.php'); 

if (isset($_GET['origin'])) { $origin = $_GET['origin']; }
else { $origin = "main"; }

?>

<div class="container">
<div class="row-fluid">
<div class="span4 offset4 well">

<form action="" method="post" name="login" id="login" class="form-login">
	<h2 class="form-signin-heading">Secure Area</h2>
	<input type="hidden" name="origin" value="<?php echo $origin; ?>" />
	<div class="input-prepend">
      		<span class="add-on"><i class="icon-user"></i></span>
		<input type="text" class="input-block-level" placeholder="Username" name="cuser" style="width: 240px;" />
	</div>
	
	<div class="input-prepend">
      		<span class="add-on"><i class="icon-key"></i></span>
		<input type="password" class="input-block-level" placeholder="Password" name="cpass" style="width: 240px;" />
	</div>
	<button class="btn btn-large btn-primary" name="signin" id="signin">Sign in</button>
</form>

</div>
</div>
</div>