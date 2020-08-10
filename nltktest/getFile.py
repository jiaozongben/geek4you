# coding:utf-8
import os
import sys
import jieba
import re
from jieba.analyse import *
import jieba.analyse # 导入关键字提取库
import pandas as pd # 导入pandas

allfile = []


def getallfile(path):
    allfilelist = os.listdir(path)
    for file in allfilelist:
        filepath = os.path.join(path, file)
        # 判断是不是文件夹
        if os.path.isdir(filepath):
            getallfile(filepath)
        allfile.append(filepath)
    return allfile


def getContent(file):
    if not os.path.isdir(file):  # 判断是否是文件夹，不是文件夹才打开
        if not file.startswith('.') and file.endswith(".md"):
            # print("filename:" + path + "/" + file)
            f = open(file, 'r', encoding='UTF-8');  # 打开文件
            iter_f = iter(f);  # 创建迭代器
            str = ""
            for line in iter_f:  # 遍历文件，一行行遍历，读取文本
                stopwords = {}.fromkeys([line.rstrip() for line in open('stopwords.txt')])
                str = str + line.rstrip()

            return str;
            # print("content:"+" ".join(str))


def jiebaTest(text):
    # jieba.enable_parallel(4)
    jieba.load_userdict('user_dict.txt')

    t = list(jieba.cut(text))
    # 中文正则表达
    r4 = u"[\u4e00-\u9fa5]+"

    new_data_list = []
    for i, val in enumerate(t):
        # sentence = re.sub(r5, '', val)
        # print(val,re.match(r4, val))
        if re.match(r4, val) != None:  # 如果正则匹配出的数据不为None, 就将此数据添加到新列表中
            new_data_list.append(val)
            # print(val)
    return new_data_list


def extract_tags_test(data):
    for keyword, weight in extract_tags(data, topK=100, withWeight=True):
        print('%s %s' % (keyword, weight))
def textrank_test(data):
    for keyword, weight in textrank(data,topK=100, withWeight=True):
        print('%s %s' % (keyword, weight))

if __name__ == '__main__':
    path = "C://Users/James/Documents/knowledge/"
    allfiles = getallfile(path)

    for item in allfiles:
        if item.endswith("todo_人生规划实现策略.md"):
            print("read file:" + item)
            text = getContent(item)


            # keys = jiebaTest(text)
            # textrank_test(text);


