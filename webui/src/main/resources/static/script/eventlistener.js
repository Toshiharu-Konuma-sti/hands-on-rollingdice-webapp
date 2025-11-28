
function showToast(timestamp) {
	const x = document.getElementById("toast");
	x.innerHTML = `JavaScript exception occurred<br>Error triggered! ID: ${timestamp}`;
	x.className = "show";
	setTimeout(function(){ 
		x.className = x.className.replace("show", ""); 
	}, 3000);
}

function generateBrowserError(event) {
	event.preventDefault();
	const timestamp = Date.now();
	showToast(timestamp);
	console.log(`[Faro Demo] Triggered JS error at: ${timestamp}`);
	throw new Error(`Handson Error: JavaScript error generated at ${timestamp}`);
}

const errorLink = document.getElementById('simulate-error-link');
if (errorLink) {
	errorLink.addEventListener('click', generateBrowserError);
}
