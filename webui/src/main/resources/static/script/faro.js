
window.addEventListener('load', () => {
  console.log("Faro: Initializing...");

  if (!window.GrafanaFaroWebSdk || !window.GrafanaFaroWebTracing) {
    console.error("Faro: Libraries not loaded correctly.");
    return;
  }

  const { initializeFaro, getWebInstrumentations } = window.GrafanaFaroWebSdk;
  const { TracingInstrumentation } = window.GrafanaFaroWebTracing;

  try {
    initializeFaro({
      url: 'http://localhost:12347/collect',
      app: {
        name: 'my-faro',
        version: '1.0.0',
      },
      instrumentations: [
        ...getWebInstrumentations(),
        new TracingInstrumentation(),
      ],
    });
    console.log("Faro: Initialization success!");
  } catch (e) {
    console.error("Faro: Initialization failed", e);
  }
});
