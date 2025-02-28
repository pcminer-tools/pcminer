#!/usr/bin/env python3
import os
import sys
import argparse

def find_non_ascii(file_path):
    """
    Check the file for any non-ASCII character.
    Returns a tuple (character, line_number, column_number) if found.
    If the file cannot be decoded (i.e. contains non-UTF8 bytes), returns (None, None, None).
    If no non-ASCII character is found, returns None.
    """
    try:
        with open(file_path, "r", encoding="utf-8") as f:
            for line_num, line in enumerate(f, start=1):
                for col_num, ch in enumerate(line, start=1):
                    if ord(ch) > 127:
                        return (ch, line_num, col_num)
    except UnicodeDecodeError:
        # If the file cannot be decoded as UTF-8, we treat it as containing non-ASCII content.
        return (None, None, None)
    return None

def main(directory):
    failed_files = []
    # Walk through the directory tree
    for root, _, files in os.walk(directory):
        for file in files:
            if file.lower().endswith('.txt'):
                full_path = os.path.join(root, file)
                result = find_non_ascii(full_path)
                if result is not None:
                    ch, line_num, col_num = result
                    if ch is None:
                        print(f"Error: Non-ASCII content found in file: {full_path} (unable to determine exact character due to decode error)")
                    else:
                        print(f"Error: Non-ASCII character '{ch}' (ord: {ord(ch)}) found in file: {full_path} at line {line_num}, column {col_num}")
                    failed_files.append(full_path)
    if failed_files:
        sys.exit("Failure: Some files contained non-ASCII characters.")
    else:
        print("Success: All files contain only ASCII characters.")

if __name__ == "__main__":
    parser = argparse.ArgumentParser(
        description="Recursively check .txt files for non-ASCII characters. "
                    "If no directory is specified, the current working directory is used."
    )
    parser.add_argument("directory", nargs="?", default=os.getcwd(), help="Directory to search (defaults to current working directory)")
    args = parser.parse_args()
    main(args.directory)