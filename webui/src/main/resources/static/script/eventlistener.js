
function putLoadingImage()
{
	// disable all links on the menu
	const olElement = document.getElementById('menu-roll');
	const liElements = olElement.getElementsByTagName('li');
	for (let i = 0; i < liElements.length; i++)
	{
		const currentLi = liElements[i];
		currentLi.classList.add('disabled-link');
		const childNodes = currentLi.children;
		for (let j = 0; j < childNodes.length; j++) {
			childNodes[j].classList.add('disabled-link');
		}
	}
	// turn it into an animated dice picture
	let img = document.getElementById('img-dice64');
	img.src = '/image/dice64_x.gif';
}

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

function updateDiceImage(event) {
	const currentValue = event.target.value;
	const image = document.getElementById('dice-preview');
	const link = document.getElementById('dice-link');

	image.src = '/image/dice32_' + currentValue + '.png';
	const baseUrl = link.getAttribute('data-base-url');
	link.href = baseUrl + '?value=' + currentValue;
}

function initDicePage() {
	const errorLink = document.getElementById('simulate-error-link');
	errorLink.addEventListener('click', generateBrowserError);
	const slider = document.getElementById('dice-slider');
	slider.addEventListener('input', updateDiceImage);
}
document.addEventListener('DOMContentLoaded', initDicePage);