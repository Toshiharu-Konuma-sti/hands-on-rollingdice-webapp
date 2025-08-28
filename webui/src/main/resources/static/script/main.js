function putLoadingImage()
{
	// disable all links on the menu
	const olElement = document.getElementById('menu-roll');
	const liElements = olElement.getElementsByTagName('li');
	for (let i = 0; i < liElements.length; i++)
	{
		const aTag = liElements[i].querySelector('a');
		aTag.classList.add('disabled-link');
	}
	// turn it into an animated dice picture
	let img = document.getElementById('img-dice64');
	img.src = '/image/dice64_x.gif';
}
