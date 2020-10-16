package com.wsfb.volunteer;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

class ArchTest {

    @Test
    void servicesAndRepositoriesShouldNotDependOnWebLayer() {

        JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("com.wsfb.volunteer");

        noClasses()
            .that()
                .resideInAnyPackage("com.wsfb.volunteer.service..")
            .or()
                .resideInAnyPackage("com.wsfb.volunteer.repository..")
            .should().dependOnClassesThat()
                .resideInAnyPackage("..com.wsfb.volunteer.web..")
        .because("Services and repositories should not depend on web layer")
        .check(importedClasses);
    }
}
