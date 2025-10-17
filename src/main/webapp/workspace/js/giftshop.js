(function(){
  if(window.mountGiftShop) return;

  function fmt(n){ return new Intl.NumberFormat('ko-KR').format(n) + '원'; }
  function ctx(p){ return (window.FE_CTX || '') + p; }

  window.mountGiftShop = function(){
	// ▼ ADDED: 스크롤바 숨김 유틸 + 즉시 적용
	function addHideScrollbar(){
	  const targets = [ document.getElementById('mainContent'), document.body, document.documentElement ]
	    .filter(Boolean);
	  targets.forEach(el => el.classList.add('hide-scrollbar'));
	  return () => targets.forEach(el => el.classList.remove('hide-scrollbar'));
	}
	const restoreScrollbar = addHideScrollbar();
	
    FE_STORE.whenReady(()=>{
      const grid = document.getElementById('shopGrid');
      const products = FE_STORE.getProducts();
      grid.innerHTML = '';
      products.forEach(p=>{
        const card = document.createElement('article');
        card.className = 'card';
        card.innerHTML = `
          <div class="thumb"><span class="ph">이미지 1:1 영역</span></div>
          <div class="brand">${p.brand}</div>
          <h3 class="name">${p.name}</h3>
          <div class="price-row"><div class="price">${fmt(p.price)}</div></div>
        `;
        card.addEventListener('click', ()=>{
          const main = document.getElementById('mainContent');
          if(main){
            fetch('detail.jsp?fragment=1').then(r=>r.text()).then(html=>{
              main.innerHTML = html;
              const cont = main.querySelector('.order-detail');
              if(cont) cont.dataset.productId = p.id;
              window.reload?.('detail');
              window.scrollTo({top:0,behavior:'smooth'});
            });
          } else {
            window.location.href = ctx('/workspace/detail.jsp?id=' + encodeURIComponent(p.id));
          }
        });
        grid.appendChild(card);
      });

      const itemsPerPage = 8;
      const cards = Array.from(grid.children);
      const totalPages = Math.max(1, Math.ceil(cards.length / itemsPerPage));
      let currentPage = 1;

      const pag = document.querySelector('.pagination');
      const prevBtn = pag.querySelector('.prev');
      const nextBtn = pag.querySelector('.next');
      const list = pag.querySelector('.page-list');

      function renderNumbers(){
        list.innerHTML = '';
        for(let i=1;i<=totalPages;i++){
          const li = document.createElement('li');
          const a = document.createElement('a');
          a.href = '#';
          a.className = 'page' + (i===currentPage ? ' is-active' : '');
          a.textContent = i;
          a.addEventListener('click', (e)=>{ e.preventDefault(); go(i); });
          li.appendChild(a);
          list.appendChild(li);
        }
      }
      function renderItems(){
        const start = (currentPage-1)*itemsPerPage;
        const end = start + itemsPerPage;
        cards.forEach((el, idx)=>{ el.style.display = (idx>=start && idx<end) ? '' : 'none'; });
        prevBtn.toggleAttribute('disabled', currentPage===1);
        nextBtn.toggleAttribute('disabled', currentPage===totalPages);
        prevBtn.classList.toggle('is-disabled', currentPage===1);
        nextBtn.classList.toggle('is-disabled', currentPage===totalPages);
        grid.scrollIntoView({behavior:'smooth', block:'start'});
      }
      function go(p){
        currentPage = Math.min(Math.max(1, p), totalPages);
        renderItems(); renderNumbers();
      }
      prevBtn.addEventListener('click', (e)=>{ e.preventDefault(); go(currentPage-1); });
      nextBtn.addEventListener('click', (e)=>{ e.preventDefault(); go(currentPage+1); });

      go(1);
	  
	  // ▼ ADDED: giftshop DOM이 사라지면 스크롤바 복원
	  const container = document.getElementById('mainContent') || document.body;
	  const ob = new MutationObserver(()=>{
	    if (!document.querySelector('.giftshop')) {
	      restoreScrollbar();
	      ob.disconnect();
	    }
	  });
	  ob.observe(container, { childList:true, subtree:true });

    });
  };

  document.addEventListener('DOMContentLoaded', ()=>{
    const present = document.querySelector('.giftshop');
    if(present) window.mountGiftShop();
  });
})();
