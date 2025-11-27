
function generateBrowserError(event) {
	event.preventDefault();
	const timestamp = Date.now();
	console.log(`[Faro Demo] Triggered JS error at: ${timestamp}`);
	throw new Error(`Handson Error: JavaScript error generated at ${timestamp}`);
}

const errorLink = document.getElementById('simulate-error-link');
if (errorLink) {
	errorLink.addEventListener('click', generateBrowserError);
}
