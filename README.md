# YACE

## Introduction

**YACE** (Yet Another Compiler Engine) exists to resolve the two principal problems in modern software development,
maintenance and migration. **YACE** is hereby described as a *compiler engine* as opposed to just a compiler due to the
presence of additional functionality to assist in the two aforementioned tasks.

Currently, there exists many tools to help write new code, such as IDEs, and tools to ensure that such changes are
compatible and tested (CI/CD). However, there doesn't seem to be a stage within many workflows revolving around
**automated** code quality and maintenance checks. Simply running the source code through an analyzer and reporting
those warnings on pull requests often do not actually resolve the warnings. Perhaps there may be formatting, but the
internal logic of the source code remains unchanged.

Some examples of criteria that determine the quality of code, such as the number of parameters in functions and
methods, the cohesion of a class, or the number of dependencies, may indeed be reported by analysis. However, many of
those issues can simply be automatically refactored before or upon commit or pull request. It seems unnecessary to
manually comb through the source code and search for pains of legibility, whereas an automated tool could resolve the
problems significantly quicker and save development and time. Granted, if new code is generated, clearly a human must
provide at least a *seal of approval*. Additionally, information that cannot be inferred in an automated context, such
as generated class names, must also be required by human input, as well. The goal is not to remove the human developer
from the equation, but rather enhance the tools of the developer for maintenance.

To solve these problems, **YACE** provides two tools. The first tool, to resolve the problem of maintenance, is a
detailed, step-by-step, refactoring plan that can automatically be applied to source code upon some event. The idea
revolves around refactorings yielding other refactoring, and thus a cascade of items that result a cleaner, leaner,
codebase. This may also ensure that the source code adheres to some degree of an unambiguous quality standard. Such
standards may then be dictated by an organization.

The second tool to resolve this problem revolves around the updating of dependencies and the manner by which code
is migrated. For instance, a library may update from version 4 to version 5, where the version increase (if following
semantic versioning) implies a **full rewrite** of the codebase. Given that the rewrite is not necessarily backwards
compatible and would most likely contain breaking changes, an automated tool must be made to refactor and **inline**
deprecated code in favor of the new usage.

Despite these features, here may be limitations and situations in which the automated tools cannot be used. For
instance, suppose that there is a library that utilizes a higher level language feature as opposed to a previous version
of the same library using an earlier language features. More specifically, a library that may have previously used
inner functions to describe automated tests would introduce the usage of decorators in the next version. In scenario
such as that, where it becomes impractical and potentially dangerous of using an automated tool, we hereby do not
recommend doing so.