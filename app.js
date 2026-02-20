const listenBtn = document.getElementById('listenBtn');
const stopBtn = document.getElementById('stopBtn');
const statusText = document.getElementById('statusText');
const transcriptText = document.getElementById('transcriptText');
const actionText = document.getElementById('actionText');
const contactsList = document.getElementById('contactsList');

const SpeechRecognition = window.SpeechRecognition || window.webkitSpeechRecognition;
const contacts = [];

function normalizePhone(raw) {
  const digits = (raw || '').replace(/\D/g, '');
  if (digits.startsWith('92') && digits.length === 12) return `+${digits}`;
  if (digits.startsWith('0') && digits.length === 11) return `+92${digits.slice(1)}`;
  if (digits.length === 10) return `+92${digits}`;
  return raw;
}

function getNumber(text) {
  const m = text.match(/(?:\+92|0)?\d{10,12}/);
  return m ? normalizePhone(m[0]) : '';
}

function includesAny(text, list) {
  return list.some((p) => text.includes(p));
}

function parseCommand(transcript) {
  const text = transcript.toLowerCase().trim();
  const number = getNumber(text);

  if (number && includesAny(text, ['save this number', 'save number', 'add contact', 'is number ko save', 'number save karo', 'contact mein save'])) {
    return { type: 'save_contact', number };
  }

  if (number && includesAny(text, ['whatsapp', "what's app", 'message this number', 'message bhejo', 'whatsapp kholo'])) {
    return { type: 'whatsapp', number };
  }

  if (number && includesAny(text, ['call', 'dial', 'call karo', 'phone lagao'])) {
    return { type: 'call', number };
  }

  if (number && includesAny(text, ['sms', 'text', 'paigham', 'msg'])) {
    const body = text.split(number.toLowerCase()).pop().trim() || 'Hello';
    return { type: 'sms', number, body };
  }

  if (includesAny(text, ['open camera', 'camera kholo'])) {
    return { type: 'open_url', url: 'https://webcamera.io/' };
  }

  return { type: 'unknown' };
}

function renderContacts() {
  contactsList.innerHTML = '';
  if (!contacts.length) {
    const li = document.createElement('li');
    li.className = 'empty';
    li.textContent = 'No contacts saved yet.';
    contactsList.appendChild(li);
    return;
  }

  contacts.forEach((c) => {
    const li = document.createElement('li');
    li.textContent = `${c.name} — ${c.number}`;
    contactsList.appendChild(li);
  });
}

function downloadVCard(name, number) {
  const vcf = `BEGIN:VCARD\nVERSION:3.0\nFN:${name}\nTEL;TYPE=CELL:${number}\nEND:VCARD`;
  const blob = new Blob([vcf], { type: 'text/vcard' });
  const url = URL.createObjectURL(blob);
  const a = document.createElement('a');
  a.href = url;
  a.download = `${name.replace(/\s+/g, '_')}.vcf`;
  a.click();
  URL.revokeObjectURL(url);
}

function execute(command) {
  switch (command.type) {
    case 'save_contact': {
      const name = `Voice Contact ${contacts.length + 1}`;
      contacts.push({ name, number: command.number });
      renderContacts();
      downloadVCard(name, command.number);
      return `Saved in browser list. Downloaded contact card for ${command.number}.`;
    }
    case 'whatsapp': {
      const clean = command.number.replace('+', '');
      window.open(`https://wa.me/${clean}`, '_blank');
      return `Opening WhatsApp chat for ${command.number}`;
    }
    case 'call': {
      window.location.href = `tel:${command.number}`;
      return `Trying to call ${command.number}`;
    }
    case 'sms': {
      const link = `sms:${command.number}?body=${encodeURIComponent(command.body)}`;
      window.location.href = link;
      return `Trying to open SMS for ${command.number}`;
    }
    case 'open_url': {
      window.open(command.url, '_blank');
      return 'Opening requested service';
    }
    default:
      return 'Sorry, command samajh nahi aaya.';
  }
}

function processTranscript(transcript) {
  transcriptText.textContent = transcript || '-';
  const command = parseCommand(transcript);
  actionText.textContent = execute(command);
}

renderContacts();

document.querySelectorAll('.chip').forEach((chip) => {
  chip.addEventListener('click', () => {
    processTranscript(chip.textContent || '');
    statusText.textContent = 'Executed sample command';
  });
});

if (!SpeechRecognition) {
  statusText.textContent = 'Speech recognition not supported in this browser. Use Chrome on Android/Desktop.';
  listenBtn.disabled = true;
} else {
  const recognition = new SpeechRecognition();
  recognition.lang = 'en-PK';
  recognition.continuous = false;
  recognition.interimResults = false;

  recognition.onstart = () => {
    statusText.textContent = 'Listening... بولیے';
    listenBtn.disabled = true;
    stopBtn.disabled = false;
  };

  recognition.onerror = (event) => {
    statusText.textContent = `Could not hear clearly. Error: ${event.error}`;
    listenBtn.disabled = false;
    stopBtn.disabled = true;
  };

  recognition.onend = () => {
    listenBtn.disabled = false;
    stopBtn.disabled = true;
  };

  recognition.onresult = (event) => {
    const transcript = event.results?.[0]?.[0]?.transcript || '';
    processTranscript(transcript);
  };

  listenBtn.addEventListener('click', () => recognition.start());
  stopBtn.addEventListener('click', () => recognition.stop());
}
