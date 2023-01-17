timeout(60) {
    node('maven-slave') {
        stage('Checkout') {
            checkout scm
        }
        stage('Running UI tests') {

            def exitCode = sh(
                    returnStatus: true,
                    script: """
                    mvn clean test -Dbrowser=$BROWSER -Dfilter=$FILTER
                    """
            )
            if (exitCode == 1) {
                currentBuild.result = 'UNSTABLE'
            }
        }
        stage('reports') {
            allure([
                    includeProperties: false,
                    jdk              : '',
                    properties       : [],
                    reportBuildPolicy: 'ALWAYS',
                    results          : [[path: 'allure-results']]
            ])
        }
    }
}