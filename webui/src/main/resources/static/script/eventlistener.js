
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

function showToast(message) {
	const x = document.getElementById("toast");
	if (!x) return;
    
	x.innerHTML = message;
	x.className = "show";
	x.style.display = "block";
	x.style.opacity = "1";

	setTimeout(function() { 
		x.className = x.className.replace("show", ""); 
		x.style.display = "none";
	}, 3000);
}

// {{{ function handleRumFetch(event)
function handleRumFetch(event) {
	// リンクのデフォルト動作（ページ遷移など）を無効化
	event.preventDefault();

	// id="rum-fetch-sleep" なら sleep モード、それ以外は normal
	const isSleep = event.currentTarget.id === 'rum-fetch-sleep';
        
	const apiBaseUrl = 'http://localhost:8182/api/v1/dices';
	let url = apiBaseUrl;
	let message = "";

	const fetchOptions = {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json'
		}
	};

	if (isSleep) {
		url += '?sleep=2';
		message = "Rolling Dice (Slow / POST)...";
	} else {
		message = "Rolling Dice (Normal / POST)...";
	}

	showToast(message);
	console.log(`[RUM Test] POST ${url}`);

	fetch(url, fetchOptions)
		.then(response => {
			if (!response.ok) {
				throw new Error(`HTTP Status: ${response.status}`);
			}
			return response.json();
		})
		.then(data => {
			console.log("[RUM Test] Success:", data);

			const diceValue = data.value;
			if (!diceValue) return;

			// -------------------------------------------------
			// 1. カレント表示（大きな画像と数値）の更新
			// -------------------------------------------------
			const currentImg = document.getElementById('img-dice64');
			if (currentImg) {
				currentImg.src = `/image/dice64_${diceValue}.png`;
			}
			const currentNumTd = document.getElementById('num-dice');
			if (currentNumTd) {
				currentNumTd.textContent = diceValue;
			}

			// -------------------------------------------------
			// 2. 履歴テーブル（Report card）への行追加
			// -------------------------------------------------
			const historyTable = document.getElementById('tbl-history');
			if (historyTable) {
				const newRow = historyTable.insertRow(1);

				// 1列目: Times (-)
				const cellTimes = newRow.insertCell(0);
				cellTimes.textContent = "-";

				// 2列目: Value (画像)
				const cellValue = newRow.insertCell(1);
				const smallImg = document.createElement('img');
				smallImg.src = `/image/dice32_${diceValue}.png`;
				cellValue.appendChild(smallImg);

				// 3列目: Updated at (---)
				const cellUpdated = newRow.insertCell(2);
				cellUpdated.textContent = "---";
			}

			// トースト表示
			showToast(`✅ Success! Dice: ${diceValue}`);
		})
		.catch(error => {
			console.error("[RUM Test] Failed:", error);
			if (error.name === 'TypeError' && error.message === 'Failed to fetch') {
				showToast("⚠️ Network/CORS Error");
			} else {
				showToast("❌ API Call Error");
			}
		});
}
// }}}

function generateBrowserError(event) {
	event.preventDefault();
	const timestamp = Date.now();
	const message = `Error triggered! ID: ${timestamp}`;
	showToast(message);
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
	const slider = document.getElementById('dice-slider');
	if (slider) {
		slider.addEventListener('input', updateDiceImage);
	}
	const rumError = document.getElementById('rum-js-error');
	if (rumError) {
		rumError.addEventListener('click', generateBrowserError);
	}	
	const rumNormal = document.getElementById('rum-fetch-normal');
	if (rumNormal) {
		rumNormal.addEventListener('click', handleRumFetch);
	}
	const rumSleep = document.getElementById('rum-fetch-sleep');
	if (rumSleep) {
		rumSleep.addEventListener('click', handleRumFetch);
	}
}
document.addEventListener('DOMContentLoaded', initDicePage);











