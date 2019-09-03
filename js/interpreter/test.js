function slowPow(a, b) {
	for (var i = 0; i < b; i++)
		a *= a;
	return a;
}

function slowPow(a, b) {
	for (var i = 0; i < b; i++)
		a *= a;
	return a;
}

function main() {
	var time = + new Date();
	for (var i = 0; i < 1; i++) {
		// 基本的表达式解析测试
		if (AstEvaluator_evaluate(ShuntingYard_compile("3+4*2/(1-5)^2^3+(7-9)^2")) != "7.0001220703125")
			throw {};
		if (AstEvaluator_evaluate(ShuntingYard_compile("3+4*2/(1-5)^2^3"))
			!= "3.0001220703125")
			throw {};
		if (AstEvaluator_evaluate(ShuntingYard_compile("3+4*2/pow(1-5,pow(2,3))"))
			!= "3.0001220703125")
			throw {};
		if (AstEvaluator_evaluate(ShuntingYard_compile("3+4*2/pow(1-5,2^3)"))
			!= "3.0001220703125")
			throw {};
		if (AstEvaluator_evaluate(ShuntingYard_compile("3+4*2/(1-5)^pow(2,3)"))
			!= "3.0001220703125")
			throw {};
		if ("6.0" != AstEvaluator_evaluate(ShuntingYard_compile("0.6*10")))
			throw {};
		if ("-189.2" != AstEvaluator_evaluate(ShuntingYard_compile("-(0.6*137-(1-0)*0+7*1+100)*1")))
			throw {};
		// 测试负号
		if (String(AstEvaluator_evaluate(ShuntingYard_compile("-log(5)"))).substring(0, 7) != "-1.6094")
			throw {};
		if (String(AstEvaluator_evaluate(ShuntingYard_compile("-1*-log(5)"))).substring(0, 6) != "1.6094")
			throw {};
		if (AstEvaluator_evaluate(ShuntingYard_compile("3+4*2/(1-5)^pow(2,3)")) != "3.0001220703125")
			throw {};
		// 测试数字类型
		var cc = ShuntingYard_compile("1+1.2");
		if (cc[0].type != NodeType_NUMBER_VALUE)
			throw {};
		if (cc[1].type != NodeType_NUMBER_VALUE)
			throw {};
		// 测试^符号的右结合性
		var a = ShuntingYard_compile("2^2^3^4^5");
		if (a[0].value != 2)
			throw {};
		if (a[1].value != 2)
			throw {};
		if (a[2].value != 3)
			throw {};
		if (a[3].value != 4)
			throw {};
		if (a[4].value != 5)
			throw {};
		for (var z = 5; z < 9; z++)
			if (a[z].value[0] != '^')
				throw {};
		// 测试变量标识符
		if (AstEvaluator_evaluate(ShuntingYard_compile("3+four*two/(1-5)^pow(2,3)"), varName => {
			if (varName == "four")
				return 4;
			if (varName == "two")
				return 2;
			return 0;
		}) != "3.0001220703125")
			throw {};
		// 关于负号优先级的测试
		if (AstEvaluator_evaluate(ShuntingYard_compile("3+4*2/-4^pow(2,3)")) != "3.0001220703125")
			throw {};
		if (AstEvaluator_evaluate(ShuntingYard_compile("3+4*2/pow(-4,pow(2,3))")) != "3.0001220703125")
			throw {};
		if (AstEvaluator_evaluate(ShuntingYard_compile("3+-2^2")) != "7.0")
			throw {};
		if (AstEvaluator_evaluate(ShuntingYard_compile("-3+-2^2")) != "1.0")
			throw {};
		// 函数测试
		// 都是转发JavaMath库的实现，所以试一下简单的成功情形就行了，不用写那么高的覆盖率
		if (String(AstEvaluator_evaluate(ShuntingYard_compile("log(5)"))).substring(0, 6) != "1.6094")
			throw {};
		if (String(AstEvaluator_evaluate(ShuntingYard_compile("abs(-9710)"))).substring(0, 4) != "9710")
			throw {};
		if (AstEvaluator_evaluate(ShuntingYard_compile("abs(9710)")) != "9710.0")
			throw {};
		if (AstEvaluator_evaluate(ShuntingYard_compile("max(9710,9711)")) != "9711.0")
			throw {};
		if (AstEvaluator_evaluate(ShuntingYard_compile("min(9710,9711)")) != "9710.0")
			throw {};
		if (AstEvaluator_evaluate(ShuntingYard_compile("min(-9,1)")) != "-9.0")
			throw {};
		if (AstEvaluator_evaluate(ShuntingYard_compile("round(10.5)")) != "11")
			throw {};
		if (AstEvaluator_evaluate(ShuntingYard_compile("floor(10.5)")) != "10.0")
			throw {};
		if (AstEvaluator_evaluate(ShuntingYard_compile("random()")) == AstEvaluator_evaluate(ShuntingYard_compile("random()")))
			throw {};// 若两次random结果一样则不通过，这里不通过的概率几乎没有，如果有...就再跑一遍试试
		// 测试隐式转型
		if (AstEvaluator_evaluate(ShuntingYard_compile("round(10.5)+2.8")) != String(13.8))
			throw {};
		if (Interpreter_calculateConfig("abcd{1+2},{2^2}{4+8/a}", name => {
			if (name == "a")
				return 2;
			throw {};
		}) != "abcd3,48")
			throw {};
	}
	time = + new Date() - time;
	alert("正确性测试用例通过，耗时:" + String(time) + "毫秒");
	/*
	time = + new Date();
	for (var i = 0; i < 1000000; i++) {
		var optimizationFence = AstEvaluator_evaluate(ShuntingYard_compile("3+4*2/(1-5)^2^3+(7-9)^2"));
		optimizationFence = AstEvaluator_evaluate(ShuntingYard_compile("3+4*2/(1-5)^2^3"));
		optimizationFence = AstEvaluator_evaluate(ShuntingYard_compile("3+4*2/-4^pow(2,3)"));
		optimizationFence = AstEvaluator_evaluate(ShuntingYard_compile("-1*-log(5)"));
		optimizationFence = AstEvaluator_evaluate(ShuntingYard_compile("3+4*2/(1-5)^pow(2,3)"));
		optimizationFence = AstEvaluator_evaluate(ShuntingYard_compile("3+4*2/pow(1-5,2^3)"));
		optimizationFence = AstEvaluator_evaluate(ShuntingYard_compile("3+4*2/pow(1-5,pow(2,3))"));
		optimizationFence = AstEvaluator_evaluate(ShuntingYard_compile("3+-2^2"));
	}

	time = + new Date() - time;
	console.log("性能测试完成，对8个表达式重复求值100万次，总耗时:" + String(time) + "毫秒，解释每条表达式平均耗时："
		+ String(time / 8000000) + "毫秒");*/
}
