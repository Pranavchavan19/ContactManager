//let stompClient = null;
//
//// Get current user and receiver from Thymeleaf-rendered inputs (or inline values)
//const currentUser = document.querySelector('input[name="toUser"]')
//  ? document.querySelector('meta[name="userEmail"]')?.content || ''
//  : '';
//const receiver = document.querySelector('input[name="toUser"]')?.value || '';
//
//function connect() {
//  const socket = new SockJS('/ws');
//  stompClient = Stomp.over(socket);
//
//  stompClient.connect({}, function (frame) {
//    console.log('✅ Connected: ' + frame);
//
//    // Subscribe to the current user's message topic
//    if (currentUser) {
//      stompClient.subscribe('/topic/messages/' + currentUser, function (messageOutput) {
//        const message = JSON.parse(messageOutput.body);
//        showMessage(message);
//      });
//    }
//  });
//}
//
//function sendMessage() {
//  const input = document.querySelector('input[name="content"]');
//  const content = input.value;
//
//  if (!receiver || content.trim() === '') {
//    alert('Receiver not set or message is empty.');
//    return;
//  }
//
//  const msg = {
//    fromUser: currentUser,
//    toUser: receiver,
//    content: content
//  };
//
//  stompClient.send('/app/chat', {}, JSON.stringify(msg));
//  input.value = '';
//}
//
//function showMessage(message) {
//  const container = document.querySelector('.hide-scrollbar');
//
//  const div = document.createElement('div');
//  div.className = message.fromUser === receiver
//    ? 'flex items-start gap-2'
//    : 'flex justify-end items-start gap-2';
//
//  const msgDiv = document.createElement('div');
//  msgDiv.className =
//    message.fromUser === receiver
//      ? 'bg-gray-700 px-2 py-2 rounded-lg shadow max-w-xs'
//      : 'bg-green-800 text-white px-2 py-2 rounded-lg shadow max-w-xs';
//
//  const p = document.createElement('p');
//  p.textContent = message.content;
//  p.className = 'text-white';
//
//  const time = document.createElement('span');
//  time.className = 'block text-xs text-white mt-1 text-right';
//  time.textContent = new Date().toLocaleString(); // optional improvement
//
//  msgDiv.appendChild(p);
//  msgDiv.appendChild(time);
//  div.appendChild(msgDiv);
//  container.appendChild(div);
//  container.scrollTop = container.scrollHeight;
//}
//
//window.addEventListener('load', connect);



//
//// websocket-chat.js
//
//document.addEventListener("DOMContentLoaded", function () {
//  console.log("Script loaded");
//
//  // ======================
//  // CONFIGURATION
//  // ======================
//  const senderId = document.getElementById("senderId")?.value;
//  const receiverId = document.getElementById("receiverId")?.value;
//  const messageInput = document.getElementById("messageInput");
//  const sendBtn = document.getElementById("sendBtn");
//  const messageContainer = document.getElementById("messageContainer");
//
//  if (!senderId || !receiverId) {
//    console.error("Missing sender or receiver ID.");
//    return;
//  }
//
//  // ======================
//  // WEBSOCKET + STOMP SETUP
//  // ======================
//  const socket = new SockJS("/ws");
//  const stompClient = Stomp.over(socket);
//
//  stompClient.connect({}, function (frame) {
//    console.log("Connected: " + frame);
//
//    // Subscribe to user's personal topic to receive messages
//    stompClient.subscribe(`/user/${senderId}/queue/messages`, function (message) {
//      const received = JSON.parse(message.body);
//      appendMessage(received.content, "received");
//    });
//  });
//
//  // ======================
//  // SEND MESSAGE
//  // ======================
//  function sendMessage(event) {
//    event.preventDefault();
//
//    const content = messageInput.value.trim();
//    if (content === "") return;
//
//    const message = {
//      senderId: senderId,
//      receiverId: receiverId,
//      content: content,
//      timestamp: new Date().toISOString()
//    };
//
//    // Send to backend via STOMP
//    stompClient.send("/app/chat", {}, JSON.stringify(message));
//
//    // Append to UI immediately
//    appendMessage(content, "sent");
//
//    // Clear input
//    messageInput.value = "";
//  }
//
//  // ======================
//  // APPEND MESSAGE TO CHAT
//  // ======================
//  function appendMessage(content, type) {
//    const msgDiv = document.createElement("div");
//    msgDiv.className = `my-2 p-2 rounded-lg w-fit max-w-xs ${
//      type === "sent" ? "ml-auto bg-green-200" : "mr-auto bg-gray-300"
//    }`;
//    msgDiv.textContent = content;
//    messageContainer.appendChild(msgDiv);
//    messageContainer.scrollTop = messageContainer.scrollHeight;
//  }
//
//  // ======================
//  // BIND SEND BUTTON
//  // ======================
//  if (sendBtn && messageInput) {
//    sendBtn.addEventListener("click", sendMessage);
//
//    // Optional: Send on Enter key
//    messageInput.addEventListener("keypress", function (e) {
//      if (e.key === "Enter") {
//        sendMessage(e);
//      }
//    });
//  } else {
//    console.error("sendBtn or messageInput not found.");
//  }
//});









document.addEventListener("DOMContentLoaded", () => {
  console.log("WebSocket chat script loaded");

  const chatBox = document.getElementById("");
  const sendBtn = document.getElementById("sendBtn");
  const form = document.querySelector("form");
  const messageInput = form.querySelector("input[name='content']");
  const toUserInput = form.querySelector("input[name='toUser']");

  if (!chatBox || !form || !messageInput || !toUserInput) {
    console.error("Missing required DOM elements");
    return;
  }

  const socket = new SockJS("/ws");
  const stompClient = Stomp.over(socket);

  stompClient.connect({}, () => {
    console.log("Connected to WebSocket");

    const toUser = toUserInput.value;

    stompClient.subscribe(`/user/queue/messages`, (message) => {
      const msg = JSON.parse(message.body);
      appendMessage(`${msg.sender}: ${msg.content}`, "incoming");
    });
  });

  form.addEventListener("submit", (e) => {
    e.preventDefault(); // prevent form reload

    const content = messageInput.value.trim();
    if (content === "") return;

    const payload = {
      to: toUserInput.value,
      content: content
    };

    stompClient.send("/app/chat", {}, JSON.stringify(payload));
    appendMessage(`Me: ${content}`, "outgoing");
    messageInput.value = "";
  });

  function appendMessage(text, type) {
    const div = document.createElement("div");
    div.className = `px-4 py-2 rounded-xl max-w-xs ${
      type === "incoming" ? "bg-gray-700 text-white" : "bg-green-800 text-white self-end"
    }`;
    div.textContent = text;
    chatBox.appendChild(div);
    chatBox.scrollTop = chatBox.scrollHeight;
  }
});
