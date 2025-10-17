/* common/js/mock-store.js */
(function () {
  if (window.FE_STORE) return;

  var ctx = window.FE_CTX || '';
  var urls = { giftProducts: ctx + '/mock/giftProducts.json' };

  var readyResolve;
  var ready = new Promise(function (res) { readyResolve = res; });
  var state = { giftProducts: [] };

  function load(url) {
    return fetch(url, { cache: 'no-store' }).then(function (r) {
      if (!r.ok) throw new Error('HTTP ' + r.status);
      return r.json();
    });
  }

  (function bootstrap() {
    load(urls.giftProducts)
      .then(function (data) { state.giftProducts = Array.isArray(data) ? data : []; })
      .catch(function (e) { console.error('[FE_STORE] giftProducts load failed', e); })
      .finally(function () { readyResolve(); });
  })();

  window.FE_STORE = {
    ready: ready,
    whenReady: function (cb) { return ready.then(cb); },
    getProducts: function () { return state.giftProducts.slice(); }
  };
})();
