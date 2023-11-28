package com.cf.processor;

import com.cf.annotation.Holder;
import com.cf.base.BaseProcessor;
import com.cf.base.Constants;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.Set;

import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;


/**
 * @ClassName: HolderProcessor
 * @Description:
 * @Author: Laughing
 * @CreateDate: 2019/9/2 13:33
 * @Version: 1.7.0
 */
@AutoService(Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes(value = {Constants.TYPE_HOLDER})
public class HolderProcessor extends BaseProcessor {
    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        super.process(set, roundEnvironment);
        mElements = processingEnv.getElementUtils();
        mMessager = processingEnv.getMessager();
        for (TypeElement element : set) {

            Set<? extends Element> roundSet = roundEnvironment.getElementsAnnotatedWith(element);
            for (Element roundElement : roundSet) {

                ClassName holderName = ClassName.get((TypeElement) roundElement);
                String builderName = holderName.simpleName() + Constants.CLASS_NAME_SUFFIX;
                Holder holderAnnotation = roundElement.getAnnotation(Holder.class);
                boolean hasLayout = !"".equals(holderAnnotation.layoutName());
                boolean forBinding = holderAnnotation.binding();

                TypeSpec.Builder builder = TypeSpec.classBuilder(builderName);
                if (forBinding) {
                    builder.addMethod(createViewHolderMethodForBinding(roundElement, hasLayout, holderAnnotation, holderName));
                } else {
                    builder.addMethod(createViewHolderMethod(roundElement, hasLayout, holderAnnotation, holderName));
                }
                TypeSpec builderSpec = builder.addMethod(itemTypeMethod(holderAnnotation))
                        .addModifiers(Modifier.PUBLIC)
                        .addSuperinterface(ParameterizedTypeName.get(ClassName.get(Constants.HOLDER_PACKAGE, Constants.HOLDER_BUILDER), holderName))
                        .addJavadoc("auto code 2")
                        .build();
                JavaFile.Builder javaFileBuilder = JavaFile.builder(getPackageName(), builderSpec);
                if (hasLayout) {
//                    javaFileBuilder.addStaticImport(ClassName.get(getPackageName() + ".R", "layout"), holderAnnotation.layoutName());
                }
                try {
                    javaFileBuilder.build().writeTo(mFiler);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return true;
    }

    private MethodSpec itemTypeMethod(Holder holderAnnotation) {
        String methodName = Constants.METHOD_ITEM_TYPE;
        TypeName returnType = TypeName.INT;
        int itemType = holderAnnotation.itemType();

        ClassName type = ClassName.get(Constants.HOLDER_PACKAGE, Constants.BASE_HOLDER_KT);
        MethodSpec.Builder builder = MethodSpec.methodBuilder(methodName)
                .addJavadoc("Auto code, get itemType")
                .returns(returnType)
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class);
        String returnStatement;
        if (itemType == -1) {
            returnStatement = "return $T.getItemTypeFromDataClass(this)";
            builder.addStatement(returnStatement, type);
        } else {
            returnStatement = "return " + itemType;
            builder.addStatement(returnStatement);
        }
        return builder.build();
    }

    private MethodSpec createViewHolderMethod(Element roundElement, boolean hasLayout, Holder holderAnnotation, ClassName holderName) {
        String methodName = Constants.METHOD_CREATE_HOLDER;
        TypeMirror returnType = roundElement.asType();
        ClassName type = ClassName.get(Constants.HOLDER_PACKAGE, Constants.BASE_HOLDER_KT);


        String layoutIdParam = hasLayout ? "," + ("$T.getLayoutInt(\"" + holderAnnotation.layoutName() + "\")") : "";
        MethodSpec methodSpec = MethodSpec.methodBuilder(methodName)
                .addJavadoc("Auto code, create ViewHolder.")
                .returns(TypeName.get(returnType))
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addParameter(ClassName.bestGuess("android.view.ViewGroup"), Constants.PARAMS_NAME_VIEW_GROUP)
                .addStatement("return new " + holderName.simpleName() + "(parent" + layoutIdParam + ")", type)
                .build();
        return methodSpec;
    }

    private MethodSpec createViewHolderMethodForBinding(Element roundElement, boolean hasLayout, Holder holderAnnotation, ClassName holderName) {
        String methodName = Constants.METHOD_CREATE_HOLDER;
        TypeMirror returnType = roundElement.asType();
        ClassName type = ClassName.get(Constants.HOLDER_PACKAGE, Constants.BASE_HOLDER_KT);


        MethodSpec methodSpec = MethodSpec.methodBuilder(methodName)
                .addJavadoc("Auto code, create ViewHolder.")
                .returns(TypeName.get(returnType))
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addParameter(ClassName.bestGuess("android.view.ViewGroup"), "parent")
                .addStatement("return new " + holderName.simpleName() + "($T.createItemView(this))", type)
                .build();
        return methodSpec;
    }

}
