
console.log("Faro: Initializing...");

if (!window.GrafanaFaroWebSdk || !window.GrafanaFaroWebTracing) {
  console.error("Faro: Libraries not loaded correctly.");
} else {
  const { initializeFaro, getWebInstrumentations } = window.GrafanaFaroWebSdk;
  const { TracingInstrumentation } = window.GrafanaFaroWebTracing;

  const instrumentationOptions = {
    propagateTraceHeaderCorsUrls: [new RegExp('http://localhost:8182/.*')],
  };

  try {
    initializeFaro({
      url: 'http://localhost:12347/collect',
      app: {
        name: 'my-faro',
        version: '1.0.0',
      },
      instrumentations: [
        ...getWebInstrumentations(),
        new TracingInstrumentation({ instrumentationOptions }),
      ],
    });
    console.log("Faro: Initialization success!");
  } catch (e) {
    console.error("Faro: Initialization failed", e);
  }
}
