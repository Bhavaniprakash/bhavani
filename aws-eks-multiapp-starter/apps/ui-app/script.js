async function callApp(appName, target) {
  target.textContent = "Loading...";

  try {
    const response = await fetch(`/${appName}`);
    if (!response.ok) {
      throw new Error(`HTTP ${response.status}`);
    }

    const data = await response.json();
    target.textContent = JSON.stringify(data, null, 2);
  } catch (error) {
    target.textContent = `Request failed: ${error.message}`;
  }
}

document.querySelectorAll(".card").forEach((card) => {
  const appName = card.dataset.app;
  const button = card.querySelector("button");
  const output = card.querySelector("pre");

  button.addEventListener("click", () => callApp(appName, output));
});
