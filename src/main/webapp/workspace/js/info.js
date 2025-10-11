(function () {
    if (window.wsSettingsMount) return;

    // 섹션 내부만 교체하는 접근차단 뷰
    function renderAccessDeniedInPlace(root, messageHTML = "이 워크스페이스에 접근할 수 없습니다.") {
        if (!root) return;
        root.innerHTML = `
      <div class="ws-ad-wrap" style="
        min-height: 420px;
        display: grid;
        place-items: center;
        background: transparent;
      ">
        <div class="ws-ad-card" style="
          width: min(680px, 92vw);
          background: var(--surface, #2A2E3E);
          border: 1px solid var(--border, #3A4059);
          border-radius: 16px;
          padding: 36px 28px;
          text-align: center;
          box-shadow: 0 12px 28px rgba(0,0,0,.45);
          color: var(--text, #E7EAF3);
        ">
          <div class="ws-ad-title" style="font-size: 24px; font-weight: 800; margin-bottom: 10px;">
            접근 권한이 없습니다
          </div>
          <p style="margin:0; color: var(--muted, #A8B3CF); font-size: 16px;">
            ${messageHTML}
          </p>
        </div>
      </div>
    `;
    }

    window.wsSettingsMount = function (root) {
        root = root || document.getElementById("ws-settings-root");
        if (!root || root.dataset.mounted === "true") return;
        root.dataset.mounted = "true";

        // ===== MOCK (서비스 연동시 data-*로 대체)
        const currentUser = { id: 1, name: "testuser123", leader: true }; // 리더면 true / 멤버면 false

        // ===== 상태
        const state = {
            name: root.dataset.wsName || "kosta-2조",
            lang: root.dataset.wsLang || "python",
            members: [
                { id: 1, name: "testuser123", email: "aaa@example.com", leader: currentUser.leader },
                { id: 2, name: "최용만",     email: "bbb@example.com", leader: !currentUser.leader },
                { id: 3, name: "정찬성",     email: "ccc@example.com", leader: false },
            ],
            filter: "",
        };

        // ===== DOM 헬퍼
        const $ = (sel) => root.querySelector(sel);
        const getMe = () => state.members.find(m => m.id === currentUser.id) || null;
        const isLeader = () => !!getMe()?.leader;

        // ===== 토스트 & 확인 모달
        function showToast(message, type = "info") {
            const key = `${type}:${message}`;
            root.querySelectorAll(`.toast[data-key="${key}"]`).forEach(n => n.remove());
            const t = document.createElement("div");
            t.className = `toast ${type}`;
            t.dataset.key = key;
            t.textContent = message;
            t.style.zIndex = 20;
            root.appendChild(t);
            requestAnimationFrame(() => t.classList.add("visible"));
            setTimeout(() => t.classList.remove("visible"), 2800);
            setTimeout(() => t.remove(), 3500);
        }
        function openConfirm(message) {
            const modal = $("#ws-confirmModal");
            const msgEl = $("#ws-confirmMessage");
            const yes = $("#ws-confirmYes");
            const no = $("#ws-confirmNo");
            return new Promise((resolve) => {
                msgEl.textContent = message;
                modal.hidden = false;
                const onYes = () => cleanup(true);
                const onNo  = () => cleanup(false);
                function cleanup(result){
                    modal.hidden = true;
                    yes.removeEventListener("click", onYes);
                    no.removeEventListener("click", onNo);
                    resolve(result);
                }
                yes.addEventListener("click", onYes);
                no.addEventListener("click", onNo);
            });
        }
        const LANG_LABEL = { cpp:"C++", java:"Java", python:"Python", javascript:"JavaScript", go:"Go", rust:"Rust" };

        // ===== 기본 정보
        const wsName = $("#ws-wsName");
        const wsLang = $("#ws-wsLang");
        const form   = $("#ws-settingsForm");
        const resetBtn = $("#ws-resetBtn");

        function loadBasics(){ wsName.value = state.name; wsLang.value = state.lang; }
        loadBasics();

        form.addEventListener("submit", async (e) => {
            e.preventDefault();
            if (!isLeader()) return;
            const newName = wsName.value.trim();
            const newLang = wsLang.value;
            if (!newName){ showToast("워크스페이스 이름을 입력하세요.","warn"); wsName.focus(); return; }
            const nameChanged = newName !== state.name;
            const langChanged = newLang !== state.lang;
            if (!nameChanged && !langChanged){ showToast("변경된 내용이 없습니다.","success"); return; }
            const lines = [];
            if (nameChanged) lines.push(`이름: '${state.name}' → '${newName}'`);
            if (langChanged) lines.push(`사용 언어: ${LANG_LABEL[state.lang]} → ${LANG_LABEL[newLang]}`);
            const ok = await openConfirm(`다음 변경사항을 적용할까요?\n${lines.join("\n")}`);
            if (!ok) return;
            const old = {name: state.name, lang: state.lang};
            state.name = newName; state.lang = newLang;
            if (nameChanged && langChanged) showToast("워크스페이스 이름과 사용 언어를 변경했어요.","success");
            else if (nameChanged) showToast(`워크스페이스 이름을 '${old.name}' → '${state.name}'로 변경했어요.`,"success");
            else showToast(`사용 언어를 ${LANG_LABEL[old.lang]} → ${LANG_LABEL[state.lang]}로 변경했어요.`,"success");
        });

        resetBtn.addEventListener("click", () => { if (isLeader()) loadBasics(); });

        // ===== 멤버 테이블
        const tbody = $("#ws-memberTbody");
        const emptyState = $("#ws-emptyState");
        const memberSearch = $("#ws-memberSearch");

        function renderMembers(){
            tbody.innerHTML = "";
            const q = state.filter.toLowerCase();
            const list = state.members.filter(m => !q || m.name.toLowerCase().includes(q) || m.email.toLowerCase().includes(q));
            emptyState.hidden = list.length !== 0;

            list.forEach(m => {
                const isSelf = m.id === currentUser.id;
                const tr = document.createElement("tr");
                const actions = isLeader() && !m.leader
                    ? `<div class="row-actions">
               <button type="button" class="btn ghost" data-delegate="${m.id}">리더 위임</button>
               <button type="button" class="btn warn"  data-kick="${m.id}">추방</button>
             </div>`
                    : "";
                tr.innerHTML = `
          <td>
            <div style="font-weight:600">${m.name}${isSelf ? " (나)" : ""}</div>
            <div style="color:#9aa6bf">${m.email}</div>
          </td>
          <td>${m.leader ? '<span class="badge leader">리더</span>' : '<span class="badge">멤버</span>'}</td>
          <td>${actions}</td>
        `;
                tbody.appendChild(tr);
            });
        }
        renderMembers();

        memberSearch.addEventListener("keydown", e => { if (e.key === "Enter") e.preventDefault(); });
        memberSearch.addEventListener("input", () => { state.filter = memberSearch.value; renderMembers(); });

        // ===== 리더 위임/추방
        let demotedOnce = false;
        tbody.addEventListener("click", async (e)=>{
            const delegateBtn = e.target.closest("button[data-delegate]");
            const kickBtn     = e.target.closest("button[data-kick]");

            if (delegateBtn){
                if (!isLeader()) return;
                const id = Number(delegateBtn.dataset.delegate);
                const target = state.members.find(m => m.id === id);
                if (!target) return;

                const wasLeader = isLeader();
                const ok = await openConfirm(`정말 ${target.name}님에게 리더 권한을 위임할까요?\n현재 리더 권한은 해제됩니다.`);
                if (!ok) return;

                state.members.forEach(m => m.leader = false);
                target.leader = true;
                renderMembers();
                showToast(`리더가 ${target.name}님으로 변경되었습니다.`,"success");

                if (wasLeader && !demotedOnce){
                    demotedOnce = true;
                    const me = getMe(); if (me) me.leader = false;
                    applyLeaderVisibility();
                    renderMembers();
                    showToast("리더 권한이 위임되었습니다. 이제 탈퇴만 가능합니다.","warn");
                }
                return;
            }

            if (kickBtn){
                if (!isLeader()) return;
                const id = Number(kickBtn.dataset.kick);
                const target = state.members.find(m => m.id === id);
                if (!target) return;

                const ok = await openConfirm(`정말 ${target.name}님을 멤버에서 추방할까요?\n이 작업은 되돌릴 수 없습니다.`);
                if (!ok) return;

                state.members = state.members.filter(m => m.id !== id);
                renderMembers();
                showToast(`${target.name}님을 추방했어요.`,"warn");
            }
        });

        // ===== 초대 모달
        const inviteModal = $("#ws-inviteModal");
        const inviteEmail = $("#ws-inviteEmail");
        const inviteOk    = $("#ws-inviteOk");
        const inviteCancel= $("#ws-inviteCancel");
        const openInvite  = $("#ws-openInvite");

        openInvite.addEventListener("click", () => {
            if (!isLeader()) return;
            inviteEmail.value = "";
            inviteModal.hidden = false;
            inviteEmail.focus();
        });
        inviteCancel.addEventListener("click", () => inviteModal.hidden = true);
        inviteOk.addEventListener("click", () => {
            if (!isLeader()) return;
            const email = inviteEmail.value.trim();
            if (!/^[^@\s]+@[^@\s]+\.[^@\s]+$/.test(email)){ showToast("올바른 이메일을 입력하세요.","warn"); inviteEmail.focus(); return; }
            if (state.members.some(m => m.email.toLowerCase() === email.toLowerCase())){ showToast("이미 존재하는 멤버입니다.","warn"); return; }
            state.members.push({ id: Date.now(), name: email.split("@")[0], email, leader:false });
            inviteModal.hidden = true; renderMembers(); showToast(`${email}로 초대 링크를 보냈어요.`,"success");
        });

        // ===== 삭제
        const deleteBtn = $("#ws-deleteWorkspaceBtn");
        deleteBtn.addEventListener("click", async () => {
            if (!isLeader()) return;
            const ok = await openConfirm(`정말 이 워크스페이스를 삭제하시겠습니까?\n이 작업은 되돌릴 수 없으며 모든 데이터가 영구적으로 삭제됩니다.`);
            if (!ok) return;
            showToast("워크스페이스를 삭제했습니다.","warn");
            setTimeout(()=>{ const card = root.querySelector(".card"); if (card) card.innerHTML = ""; }, 900);
        });

        // ===== 탈퇴 (섹션 내부만 권한없음 화면으로 교체)
        const leaveBtn = $("#ws-leaveWorkspaceBtn");
        leaveBtn.addEventListener("click", async () => {
            const me = getMe();
            if (!me){ showToast("이미 탈퇴된 사용자입니다.","warn"); return; }

            const others = state.members.filter(m => m.id !== currentUser.id);
            if (me.leader && others.length > 0){ showToast("리더 위임 후 탈퇴할 수 있어요.","warn"); return; }

            const ok = await openConfirm(`정말 워크스페이스에서 나가시겠습니까?\n내 정보와 권한은 이 워크스페이스에서 제거됩니다.`);
            if (!ok) return;

            state.members = state.members.filter(m => m.id !== currentUser.id);
            renderMembers();
            showToast("워크스페이스를 탈퇴했어요.","success");

            if (state.members.length === 0){
                showToast("남은 인원이 없어 워크스페이스가 자동으로 삭제되었습니다.","warn");
                setTimeout(()=>{ const card = root.querySelector(".card"); if (card) card.innerHTML = ""; }, 800);
            }

            // ✅ 섹션 내부만 접근차단 화면으로 변경
            setTimeout(() => {
                renderAccessDeniedInPlace(
                    root,
                    "이 워크스페이스에서 <b>탈퇴</b>하여 더 이상 접근할 수 없습니다."
                );
            }, 600);
        });

        // ===== 리더 전용 영역 보이기/숨기기
        const basicActions = $("#ws-basicActions");
        const dangerBanner = $("#ws-dangerBanner");
        const dangerZone   = $("#ws-dangerZone");
        function applyLeaderVisibility(){
            const leader = isLeader();
            [basicActions, dangerBanner, dangerZone, openInvite].forEach(el => {
                if (!el) return;
                if (leader) el.classList.remove("hidden");
                else el.classList.add("hidden");
            });
            wsName.disabled = !leader;
            wsLang.disabled = !leader;
        }
        applyLeaderVisibility();
    };

    // 정적 include로 열렸을 때 자동 mount
    document.addEventListener("DOMContentLoaded", () => {
        const root = document.getElementById("ws-settings-root");
        if (root) window.wsSettingsMount(root);
    });
})();
