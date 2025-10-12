(function(){
  if (window.mountOrderDetail) return;

  function fmt(n){ return new Intl.NumberFormat('ko-KR').format(n) + '원'; }
  function qs(sel, root=document){ return root.querySelector(sel); }
  function qsa(sel, root=document){ return Array.from(root.querySelectorAll(sel)); }
  function getParam(name){ return new URLSearchParams(location.search).get(name); }
  function ctx(p){ return (window.FE_CTX || '') + p; }

  async function loadProductsFallback(){
    // 배치에 맞게 경로 수정하세요. (예: /workspace/data/giftProducts.json)
    const res = await fetch(ctx('/workspace/giftProducts.json')).catch(()=>null);
    if(!res || !res.ok) return [];
    return res.json();
  }

  async function getProducts(){
    if (window.FE_STORE?.whenReady) {
      await new Promise(res => window.FE_STORE.whenReady(res));
      return window.FE_STORE.getProducts?.() || [];
    }
    return await loadProductsFallback();
  }

  function getReward(){
    // 서버 통합 시 API로 대체 가능
    return typeof window.USER_REWARD === 'number' ? window.USER_REWARD : 60000;
  }

  function setReward(v){ window.USER_REWARD = v; }

  function bindModal(root){
    let current = null;
    function open(el){
      current = el; el.setAttribute('aria-hidden','false');
      setTimeout(()=> el.querySelector('[data-close], .btn-primary')?.focus(), 0);
      document.addEventListener('keydown', escClose);
    }
    function close(){
      if(!current) return; current.setAttribute('aria-hidden','true'); current = null;
      qs('#payBtn', root)?.focus();
      document.removeEventListener('keydown', escClose);
    }
    function escClose(e){ if(e.key==='Escape') close(); }
    qsa('.modal', root).forEach(m=>{
      m.addEventListener('click', e=>{
        if(e.target===m || e.target.hasAttribute('data-close')) close();
      });
    });
    return { open, close };
  }

  function fillProduct(root, p){
    qs('[data-field="brand"]', root).textContent = p.brand || '';
    qs('[data-field="name"]', root).textContent  = p.name  || '';
    qs('[data-field="price"]', root).textContent = fmt(p.price || 0);
    const img = qs('.thumb img', root);
    const ph  = qs('.thumb .ph', root);
    if (p.img) {
      img.src = p.img; img.alt = p.name || '상품 이미지'; img.style.display = 'block';
      ph.style.display = 'none';
    } else {
      img.style.display = 'none'; ph.style.display = 'flex';
    }
  }

  window.mountOrderDetail = async function(){
    const root = document.querySelector('.order-detail');
    if (!root) return;

    // 상품 식별
    let id = root.dataset.productId || getParam('id') || '';
    const products = await getProducts();
    const product = products.find(x => String(x.id) === String(id)) || products[0] || { id:'', brand:'', name:'', price:0, img:'' };

    fillProduct(root, product);

    const { open, close } = bindModal(document);

    // 결제 버튼
    const payBtn = qs('#payBtn', root);
    const confirmMsg = qs('#confirmMsg', root);
    const insufMsg   = qs('#insufMsg', root);
    const successMsg = qs('#successMsg', root);

    payBtn?.addEventListener('click', ()=>{
      const price = Number(product.price || 0);
      const reward = Number(getReward());
      if (reward >= price) {
        const rest = reward - price;
        confirmMsg.innerHTML = `현재 리워드는 <strong>${fmt(reward)}</strong>으로 결제 가능합니다.<br>리워드 <strong>${fmt(price)}</strong> 차감되어 잔액 <strong>${fmt(rest)}</strong>이 남습니다. 결제하시겠습니까?`;
        open(qs('#confirmModal', root));
      } else {
        insufMsg.innerHTML = `현재 리워드는 <strong>${fmt(reward)}</strong>으로 결제 금액 <strong>${fmt(price)}</strong>을 결제할 수 없습니다.`;
        open(qs('#insufModal', root));
      }
    });

    // 확인 결제
    qs('#confirmPayBtn', root)?.addEventListener('click', ()=>{
      const price = Number(product.price || 0);
      const reward = Number(getReward());
      const rest = reward - price;
      setReward(rest);
      close();
      successMsg.innerHTML = `리워드 <strong>${fmt(price)}</strong> 결제 완료! 남은 리워드 <strong>${fmt(rest)}</strong>입니다.`;
      open(qs('#successModal', root));
    });
	
	// 목록으로 → giftshop
	qs('#backToList', root)?.addEventListener('click', (e)=>{
	  const main = document.getElementById('mainContent');
	  if (!main) return;               // 일반 페이지: 기본 href로 이동
	  e.preventDefault();              // SPA일 땐 화면 조각으로 전환
	  fetch('giftshop.jsp?fragment=1') // detail.jsp와 같은 폴더면 이 상대경로로 OK
	    .then(r=>r.text())
	    .then(html=>{
	      main.innerHTML = html;
	      if (window.App?.reload) App.reload('giftshop');
	      else window.reload?.('giftshop');
	      window.scrollTo({ top: 0, behavior: 'smooth' });
	    });
	});
  };

  // 단독 접근 시 자동 마운트
  document.addEventListener('DOMContentLoaded', ()=> {
    const present = document.querySelector('.order-detail');
    if (present) window.mountOrderDetail();
  });
})();
