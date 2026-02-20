#!/usr/bin/env python3
import json
from pathlib import Path

REQUIRED = {"text", "intent", "language", "target_users", "entities"}


def validate(path: Path):
    intents = set()
    count = 0
    for i, line in enumerate(path.read_text(encoding="utf-8").splitlines(), start=1):
        if not line.strip():
            continue
        obj = json.loads(line)
        missing = REQUIRED - set(obj)
        if missing:
            raise ValueError(f"{path}:{i} missing keys: {sorted(missing)}")
        if not isinstance(obj["entities"], dict):
            raise ValueError(f"{path}:{i} entities must be object")
        intents.add(obj["intent"])
        count += 1
    return count, intents


def main():
    train = Path("data/voice_intents_train.jsonl")
    dev = Path("data/voice_intents_dev.jsonl")
    c1, i1 = validate(train)
    c2, i2 = validate(dev)
    print(f"train_samples={c1}")
    print(f"dev_samples={c2}")
    print(f"intents={len(i1|i2)}")


if __name__ == "__main__":
    main()
