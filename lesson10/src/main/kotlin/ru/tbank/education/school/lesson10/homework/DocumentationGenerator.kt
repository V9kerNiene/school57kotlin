package ru.tbank.education.school.lesson10.homework

import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation

object DocumentationGenerator {
    fun generateDoc(obj: Any): String {
        var res = ""
        val specClass = obj::class
        if (specClass.hasAnnotation<InternalApi>()) {
            return "Документация скрыта (InternalApi)."
        }
        if (specClass.hasAnnotation<DocClass>()) {
            val classAnnotation = specClass.findAnnotation<DocClass>()!!
            res += "=== Документация: ${specClass.simpleName} ===\n"
            res += "Описание: ${classAnnotation.description}\n"
            res += "Автор: ${classAnnotation.author}\n"
            res += "Версия: ${classAnnotation.version}\n\n"

            val properties = specClass.declaredMemberProperties
                .filterNot { it.hasAnnotation<InternalApi>() }
                .filterNot { it.name.contains("component") }

            if (properties.isNotEmpty()) {
                res += "--- Свойства ---\n"

                for (i in properties) {
                    res += "- ${i.name}\n"
                    if (i.hasAnnotation<DocProperty>()) {
                        res += "  Описание: ${i.findAnnotation<DocProperty>()!!.description}\n"
                        if (i.findAnnotation<DocProperty>()!!.example != "") res += "  Пример: ${i.findAnnotation<DocProperty>()!!.example}\n"
                    }
                }
            }

            val methods = specClass.declaredFunctions.filterNot { it.hasAnnotation<InternalApi>() }.filterNot {
                it.name in setOf("toString", "equals", "hashCode", "copy") || it.name.contains("component")
            }

            if (methods.isNotEmpty()) {
                res += "\n--- Методы ---\n"

                for (i in methods) {
                    var params = ""
                    val rawParams = i.parameters.filterNot { it.hasAnnotation<InternalApi>() }
                    val paramsMap = mutableMapOf<String, String>()
                    val amountOfParams = rawParams.size

                    for (param in 1..<amountOfParams) {
                        if (param > 1) {
                            params += ", "
                        }

                        paramsMap[rawParams[param].name!!] = rawParams[param].findAnnotation<DocParam>()?.description ?: "Нет описания"
                        params += "${rawParams[param].name}: ${rawParams[param].type}"
                    }
                    res += "- ${i.name}(${params})\n"
                    res += "  Описание: ${i.findAnnotation<DocMethod>()?.description ?: "Нет описания"}\n"
                    if (paramsMap.isNotEmpty()) {
                        res += "  Параметры:\n"
                        for (param in paramsMap) {
                            res += "    - ${param.key}: ${param.value}\n"
                        }
                    }
                    res += "  Возвращает: ${i.findAnnotation<DocMethod>()?.returns ?: "Нет описания"}\n"
                }
            }


        } else {
            return "Нет документации для класса."
        }
        return res
    }
}

