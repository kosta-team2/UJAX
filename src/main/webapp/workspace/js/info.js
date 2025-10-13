    window.initInfo = async function () {
    const root = document.getElementById("ws-settings-root");
        if (!root) return;

        // ===== DOM refs =====
        const wsName = root.querySelector("#ws-wsName");
        const wsLang = root.querySelector("#ws-wsLang");
        const form = root.querySelector("#ws-settingsForm");
        const resetBtn = root.querySelector("#ws-resetBtn");
        const deleteBtn = root.querySelector("#ws-deleteWorkspaceBtn");
        const inviteModal = root.querySelector("#ws-inviteModal");
        const openInvite = root.querySelector("#ws-openInvite");
        const inviteEmail = root.querySelector("#ws-inviteEmail");
        const inviteCancel = root.querySelector("#ws-inviteCancel");
        const inviteOk = root.querySelector("#ws-inviteOk");
        const tbody = root.querySelector("#ws-memberTbody");
        const leaveBtn = root.querySelector("#ws-leaveWorkspaceBtn");
        const confirmModal = root.querySelector("#ws-confirmModal");
        const confirmMessage = root.querySelector("#ws-confirmMessage");
        const confirmYes = root.querySelector("#ws-confirmYes");
        const confirmNo = root.querySelector("#ws-confirmNo");

        // ===== JSP 데이터 =====
        const isLeader = root.dataset.isLeader === "true";

        // ===== Toast =====
        function showToast(message, type = "info") {
            const key = `${type}:${message}`;
            root.querySelectorAll(`.toast[data-key="${key}"]`).forEach((n) => n.remove());

            const toast = document.createElement("div");
            toast.className = `toast ${type}`;
            toast.textContent = message;
            toast.dataset.key = key;
            root.appendChild(toast);

            requestAnimationFrame(() => toast.classList.add("visible"));
            setTimeout(() => toast.classList.remove("visible"), 2800);
            setTimeout(() => toast.remove(), 3500);
        }

        // ===== Confirm Modal =====
        function openConfirm(message) {
            return new Promise((resolve) => {
                confirmMessage.textContent = message;
                confirmModal.hidden = false;

                const onYes = () => cleanup(true);
                const onNo = () => cleanup(false);

                confirmYes.addEventListener("click", onYes);
                confirmNo.addEventListener("click", onNo);

                function cleanup(result) {
                    confirmModal.hidden = true;
                    confirmYes.removeEventListener("click", onYes);
                    confirmNo.removeEventListener("click", onNo);
                    resolve(result);
                }
            });
        }

        const LANG_LABEL = {
            cpp: "C++",
            java: "Java",
            python: "Python",
            javascript: "JavaScript",
            go: "Go",
            rust: "Rust",
        };

        // ===== 기본정보 수정 =====
        form?.addEventListener("submit", async (e) => {
            e.preventDefault();
            if (!isLeader) return;

            const newName = wsName.value.trim();
            const newLang = wsLang.value;
            if (!newName) {
                showToast("워크스페이스 이름을 입력하세요.", "warn");
                wsName.focus();
                return;
            }

            const oldName = wsName.defaultValue;
            const oldLang = wsLang.dataset.initial || wsLang.value;
            const nameChanged = newName !== oldName;
            const langChanged = newLang !== oldLang;
            if (!nameChanged && !langChanged) {
                showToast("변경된 내용이 없습니다.", "info");
                return;
            }

            const lines = [];
            if (nameChanged) lines.push(`이름: '${oldName}' → '${newName}'`);
            if (langChanged)
                lines.push(`사용 언어: ${LANG_LABEL[oldLang]} → ${LANG_LABEL[newLang]}`);

            const ok = await openConfirm(`다음 변경사항을 적용할까요?\n${lines.join("\n")}`);
            if (!ok) return;

            // 실제 요청 예시
            // await fetch('/workspace/update', { method: 'POST', body: new FormData(form) });

            showToast("워크스페이스 정보를 변경했습니다.", "success");
        });

        resetBtn?.addEventListener("click", () => {
            wsName.value = wsName.defaultValue;
            wsLang.value = wsLang.dataset.initial || wsLang.value;
            showToast("변경 내용을 취소했습니다.", "info");
        });

        // ===== 리더 위임 / 추방 =====
        tbody?.addEventListener("click", async (e) => {
            const delegateBtn = e.target.closest("[data-delegate]");
            const kickBtn = e.target.closest("[data-kick]");

            if (delegateBtn) {
                const name = delegateBtn.closest("tr").querySelector("td div")?.textContent.trim();
                const ok = await openConfirm(
                    `정말 ${name}님에게 리더 권한을 위임할까요?\n현재 리더 권한은 해제됩니다.`
                );
                if (!ok) return;

                // 실제 요청 예시
                // await fetch(`/workspace/delegate?id=${delegateBtn.dataset.delegate}`, { method: 'POST' });

                showToast(`리더가 ${name}님으로 변경되었습니다.`, "success");
            }

            if (kickBtn) {
                const name = kickBtn.closest("tr").querySelector("td div")?.textContent.trim();
                const ok = await openConfirm(
                    `정말 ${name}님을 멤버에서 추방할까요?\n이 작업은 되돌릴 수 없습니다.`
                );
                if (!ok) return;

                // 실제 요청 예시
                // await fetch(`/workspace/kick?id=${kickBtn.dataset.kick}`, { method: 'POST' });

                showToast(`${name}님을 추방했어요.`, "warn");
            }
        });

        // ===== 초대 모달 =====
        openInvite?.addEventListener("click", () => {
            inviteEmail.value = "";
            inviteModal.hidden = false;
            inviteEmail.focus();
        });
        inviteCancel?.addEventListener("click", () => (inviteModal.hidden = true));
        inviteOk?.addEventListener("click", async () => {
            const email = inviteEmail.value.trim();
            if (!/^[^@\s]+@[^@\s]+\.[^@\s]+$/.test(email)) {
                showToast("올바른 이메일을 입력하세요.", "warn");
                inviteEmail.focus();
                return;
            }

            // 실제 요청 예시
            // await fetch('/workspace/invite', { method: 'POST', body: JSON.stringify({ email }) });

            showToast(`${email}로 초대 링크를 보냈어요.`, "success");
            inviteModal.hidden = true;
        });

        // ===== 워크스페이스 삭제 =====
        deleteBtn?.addEventListener("click", async () => {
            const ok = await openConfirm(
                `정말 이 워크스페이스를 삭제하시겠습니까?\n이 작업은 되돌릴 수 없습니다.`
            );
            if (!ok) return;

            // 실제 요청 예시
            // await fetch('/workspace/delete', { method: 'POST' });

            showToast("워크스페이스 삭제 요청을 보냈습니다.", "warn");
        });

        // ===== 탈퇴 =====
        leaveBtn?.addEventListener("click", async () => {
            const ok = await openConfirm(
                `정말 워크스페이스에서 나가시겠습니까?\n내 정보와 권한은 이 워크스페이스에서 제거됩니다.`
            );
            if (!ok) return;

            // 실제 요청 예시
            // await fetch('/workspace/leave', { method: 'POST' });

            showToast("워크스페이스 탈퇴 요청을 보냈습니다.", "success");
        });
    };
