document.addEventListener('DOMContentLoaded', async () => {
    const main = document.getElementById('mainContent');
    const sidebarContainer = document.querySelector('.workspace-list');

    try {
        const res = await fetch('../mock/workspace.json');
        const workspaces = await res.json();

        const commonPages = [
            { label: '워크스페이스 홈', page: 'home' },
            { label: '공지 전체 보기', page: 'notice' },
            { label: '문제 전체 보기', page: 'problem' },
            { label: '워크스페이스 관리', page: 'info' }
        ];

        sidebarContainer.innerHTML = workspaces
            .map(ws => `
        <div class="workspace-toggle" data-workspace="${ws.id}">
          <button class="workspace-name">${ws.name}</button>
          <div class="workspace-menu">
            ${commonPages.map(p => `
              <button class="nav-btn" data-page="${p.page}" data-workspace="${ws.id}">
                ${p.label}
              </button>
            `).join('')}
          </div>
        </div>
      `).join('');

        // 워크스페이별 토글 선택시 워크스페이스 홈 불러오기
        document.querySelectorAll('.workspace-name').forEach(btn => {
            btn.addEventListener('click', e => {
                const toggle = e.target.closest('.workspace-toggle');
                const menu = toggle.querySelector('.workspace-menu');
                menu.classList.toggle('open');
                btn.classList.toggle('active');

                if (menu.classList.contains('open')) {
                    const homeBtn = menu.querySelector('[data-page="home"]');
                    if (homeBtn) homeBtn.click();
                }
            });
        });

        // 워크스페이스 별 메뉴 버튼 선택시
        document.querySelectorAll('.nav-btn').forEach(btn => {
            btn.addEventListener('click', async () => {
                const page = btn.dataset.page;
                const workspaceId = btn.dataset.workspace;

                // todo workspaceId에 맞는 page? 이건 요청 부분에서 사용해야할듯
                try {
                    const res = await fetch(`${page}.jsp`);
                    const html = await res.text();
                    main.innerHTML = html;

                    const mountName = `ws${page.charAt(0).toUpperCase() + page.slice(1)}Mount`;
                    if (typeof window[mountName] === 'function') {
                        window[mountName](main.querySelector(`#ws-settings-root`) || main);
                    }

                    // 페이지 비동기 이동시 마다 맞는 js 호출
                    if (typeof reload === 'function') reload(page);

                    console.log(`✅ ${workspaceId}번 워크스페이스의 ${page}.jsp 로드 완료`);
                } catch (err) {
                    main.innerHTML = `<p style="color:red;">❌ ${page}.jsp 로드 실패</p>`;
                }

                // 활성화 표시 갱신
                document.querySelectorAll('.nav-btn').forEach(b => b.classList.remove('active'));
                btn.classList.add('active');
            });
        });
    } catch (err) {
        console.error('워크스페이스 목록 로드 실패:', err);
        sidebarContainer.innerHTML = `<p style="color:red;">❌ 워크스페이스를 불러올 수 없습니다.</p>`;
    }
});
